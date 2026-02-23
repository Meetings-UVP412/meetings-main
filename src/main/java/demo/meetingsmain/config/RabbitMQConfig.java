package demo.meetingsmain.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "meetings-exchange";
    public static final String ROUTING_KEY_CHUNK_DOWNLOADED = "chunk.downloaded";

    @Bean
    public TopicExchange meetingsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }
}
