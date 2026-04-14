package demo.meetingsmain.listeners;

import demo.eventscontract.events.CleanupMeetingEvent;
import demo.meetingsmain.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CleanupEventListener {
    private final RedisTemplate<String, Object> redisTemplate;
    private final Logger log = LoggerFactory.getLogger(CleanupEventListener.class);

    public CleanupEventListener(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.CLEANUP_QUEUE)
    public void handleCleanup(CleanupMeetingEvent event) {
        String uuid = event.meetingUuid();
        Integer ord = event.ord();

        while (ord > 0) {
            redisTemplate.delete(uuid + "_chunk_" + ord);
            ord--;
        }

        redisTemplate.delete(uuid + "_result");

        log.info("Данные встречи {} успешно удалены из Redis", uuid);
    }
}
