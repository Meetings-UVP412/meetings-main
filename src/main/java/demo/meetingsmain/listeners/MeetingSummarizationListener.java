package demo.meetingsmain.listeners;

import com.rabbitmq.client.Channel;
import demo.eventscontract.events.MeetingSummarizationEvent;
import demo.meetingscontracts.dto.MeetingStatus;
import demo.meetingsmain.config.RabbitMQConfig;
import demo.meetingsmain.service.MeetingService;
import demo.meetingsmain.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class MeetingSummarizationListener {
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private RedisService redisService;

    private static final Logger log = LoggerFactory.getLogger(MeetingSummarizationListener.class);
    private static final String QUEUE_NAME_MEETING_SUMMARIZED = "meeting-summarization-queue";
    private static final String QUEUE_NAME_MEETING_SUMMARIZED_DLQ = "meeting-summarization-queue.dlq";

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = QUEUE_NAME_MEETING_SUMMARIZED, durable = "true", arguments = {
                    @Argument(name = "x-dead-letter-exchange", value = "dlx-exchange"),
                    @Argument(name = "x-dead-letter-routing-key", value = "dlq.meetings-summarization")}),
            exchange = @Exchange(name = RabbitMQConfig.EXCHANGE_NAME, type = "topic", durable = "true"),
            key = RabbitMQConfig.ROUTING_KEY_MEETING_SUMMARIZATION))
    public void handleFinishedSummarization(@Payload MeetingSummarizationEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("Got meeting summarization from queue: {} \nSummary: {}", event.uuid(), event.summary());
            changeStatusToProcessed(event.uuid()); // change meeting status to PROCEED

            redisService.scheduleCleanup(event.uuid(), event.ord(), 3 * 60 * 60 * 1000); // sent to rabbit to drop meeting from redis in 3 hours
            log.info("Sent message to rabbitMQ, meeting: {} will be dropped from redis in 3 hours", event.uuid());

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Failed to process event: {}. Sending to DLQ.", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = QUEUE_NAME_MEETING_SUMMARIZED_DLQ, durable = "true"),
            exchange = @Exchange(name = "dlx-exchange", type = "topic", durable = "true"),
            key = "dlq.meetings-summarization"))
    public void handleDlqMessagesCreate(Object failedMessage) {
        log.error("!!! Received message in DLQ meeting-summarization: {}", failedMessage);
    }

    private void changeStatusToProcessed(String uuid) {
        meetingService.changeMeetingStatus(uuid, MeetingStatus.PROCESSED);
    }
}
