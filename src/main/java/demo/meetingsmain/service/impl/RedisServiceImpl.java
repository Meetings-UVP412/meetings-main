package demo.meetingsmain.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.eventscontract.events.ChunkDownloadedEvent;
import demo.eventscontract.events.CleanupMeetingEvent;
import demo.meetingscontracts.dto.MeetingResponse;
import demo.meetingscontracts.dto.MeetingStatus;
import demo.meetingscontracts.exceptions.IllegalArgumentException;
import demo.meetingscontracts.exceptions.ResourceNotFoundException;
import demo.meetingsmain.config.RabbitMQConfig;
import demo.meetingsmain.domain.Meeting;
import demo.meetingsmain.repository.MeetingRepository;
import demo.meetingsmain.service.RedisService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MeetingRepository meetingRepository;

    private final StringRedisTemplate stringRedisTemplate;
    private final RabbitTemplate rabbitTemplate;
    private static final Logger log = LoggerFactory.getLogger(RedisServiceImpl.class);
    private final MeetingServiceImpl meetingService;

    public RedisServiceImpl(StringRedisTemplate stringRedisTemplate, RabbitTemplate rabbitTemplate, MeetingServiceImpl meetingService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.rabbitTemplate = rabbitTemplate;
        this.meetingService = meetingService;
    }

    @Transactional
    public void saveAudio(Integer ord, Boolean isLast, UUID uuid, byte[] audioData) {
        String fullPath = uuid.toString() + "_chunk_" + ord;

        // Check meeting exists and have NEW status
        MeetingResponse meeting = meetingService.getMeeting(uuid.toString());
        if (meeting.status() != MeetingStatus.NEW) {
            throw new IllegalArgumentException();
        }

        // change status to END if isLast == true
        if (isLast) {
            Optional<Meeting> optionalMeeting = meetingRepository.findById(uuid.toString());

            if (optionalMeeting.isPresent()) {
                Meeting newMeeting = optionalMeeting.get();
                newMeeting.setStatus(MeetingStatus.END);
                meetingRepository.save(newMeeting);

                log.info("Changed meeting status to END: {}", uuid);
            } else {
                throw new ResourceNotFoundException("Meeting", uuid);
            }
        }

        redisTemplate.opsForValue().set(fullPath, audioData); // save audio in redis
        log.info("Saved new chunk: UUID: {} ord: {} isLast: {}", uuid, ord, isLast);

        // publish event: chunk downloaded
        ChunkDownloadedEvent event = new ChunkDownloadedEvent(
                uuid,
                ord,
                isLast,
                0
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_CHUNK_DOWNLOADED, event);
        log.info("Sent new message ro rabbitmq about new chunk: {} ", fullPath);
    }

    public byte[] getAudio(String key) {
        log.info("Retrieve chunk with key: {}", key);
        return (byte[]) redisTemplate.opsForValue().get(key);
    }

    public void deleteAudio(String key) {
        redisTemplate.delete(key);
    }

    @Transactional
    public void updateTranscriptionForMeeting(String result, UUID uuid) {
        String fullPath = uuid.toString() + "_result";

        String currentValue = stringRedisTemplate.opsForValue().get(fullPath);

        if (currentValue == null || currentValue.isEmpty()) {
            stringRedisTemplate.opsForValue().set(fullPath, result);
        } else {
            stringRedisTemplate.opsForValue().set(fullPath, currentValue + "\n" + result);
        }

        log.info("Updated transcription for meeting: {}", uuid);
    }

    public String getMeetingTranscription(UUID uuid) {
        String fullPath = uuid.toString() + "_result";
        String fullText = stringRedisTemplate.opsForValue().get(fullPath);
        log.info("GET meeting transcription: {}", fullText);
        return fullText;
    }

    public void scheduleCleanup(String meetingUuid, Integer ord, long delayMs) throws JsonProcessingException {
        CleanupMeetingEvent event = new CleanupMeetingEvent(meetingUuid, ord);

        Message message = MessageBuilder
                .withBody(new ObjectMapper().writeValueAsBytes(event))
                .setHeader("x-delay", delayMs)
                .setContentType("application/json")
                .build();

        rabbitTemplate.send(RabbitMQConfig.DELAYED_CLEANUP_EXCHANGE, RabbitMQConfig.CLEANUP_ROUTING_KEY, message);

        log.info("Запланирована очистка встречи: {} через {} мс", meetingUuid, delayMs);
    }
}
