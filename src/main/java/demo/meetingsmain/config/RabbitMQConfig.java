package demo.meetingsmain.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "meetings-exchange";
    public static final String ROUTING_KEY_CHUNK_DOWNLOADED = "chunk.downloaded";
    public static final String ROUTING_KEY_MEETING_SUMMARIZATION = "meeting.summarized";

    public static final String DELAYED_CLEANUP_EXCHANGE = "delayed-cleanup-exchange";
    public static final String CLEANUP_ROUTING_KEY = "cleanup.meeting";
    public static final String CLEANUP_QUEUE = "cleanup.queue";

    @Bean
    public TopicExchange meetingsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public CustomExchange delayedCleanupExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_CLEANUP_EXCHANGE, "x-delayed-message", true, false, args);
    }

    @Bean
    public Queue cleanupQueue() {
        return QueueBuilder.durable(CLEANUP_QUEUE).build();
    }

    @Bean
    public Binding bindCleanupQueue(Queue cleanupQueue, CustomExchange delayedCleanupExchange) {
        return BindingBuilder.bind(cleanupQueue)
                .to(delayedCleanupExchange)
                .with(CLEANUP_ROUTING_KEY)
                .noargs();
    }
}
