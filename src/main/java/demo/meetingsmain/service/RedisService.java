package demo.meetingsmain.service;

import demo.eventscontract.events.ChunkDownloadedEvent;
import demo.meetingscontracts.dto.MeetingResponse;
import demo.meetingscontracts.dto.MeetingStatus;
import demo.meetingscontracts.exceptions.IllegalArgumentException;
import demo.meetingsmain.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private final RabbitTemplate rabbitTemplate;

    private final MeetingService meetingService;

    public RedisService(RabbitTemplate rabbitTemplate, MeetingService meetingService) {
        this.rabbitTemplate = rabbitTemplate;
        this.meetingService = meetingService;
    }

    public void saveAudio(Integer ord, Boolean isLast, UUID uuid, byte[] audioData) {
        String fullPath = uuid.toString() + "_chunk_" + ord;

        // Check meeting exists and have NEW status
        MeetingResponse meeting = meetingService.getMeeting(uuid);
        if (meeting.status() != MeetingStatus.NEW) {
            throw new IllegalArgumentException();
        }

        redisTemplate.opsForValue().set(fullPath, audioData); // save audio in redis

        // publish event: chunk downloaded
        ChunkDownloadedEvent event = new ChunkDownloadedEvent(
                uuid,
                ord,
                isLast,
                0
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_CHUNK_DOWNLOADED, event);
    }

    public byte[] getAudio(String key) {
        return (byte[]) redisTemplate.opsForValue().get(key);
    }

    public void deleteAudio(String key) {
        redisTemplate.delete(key);
    }
}
