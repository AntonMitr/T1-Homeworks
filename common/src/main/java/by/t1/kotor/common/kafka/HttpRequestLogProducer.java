package by.t1.kotor.common.kafka;

import by.t1.kotor.common.model.dto.HttpRequestLogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class HttpRequestLogProducer {
    private final KafkaTemplate<String, Object> errortKafkaTemplate;
    @Value("${t1.kafka.topic.service_logs}")
    private String TOPIC;
    @Value("${spring.application.name}")
    private String KEY;

    public void sendToTopic(HttpRequestLogMessage logMessage) {
        try {
            Message<HttpRequestLogMessage> message = MessageBuilder
                    .withPayload(logMessage)
                    .setHeader(KafkaHeaders.TOPIC, TOPIC)
                    .setHeader(KafkaHeaders.KEY, KEY)
                    .setHeader("type", "INFO")
                    .build();

            errortKafkaTemplate.send(message).get();
        } catch (Exception e) {
            log.error("Error sending message", e);
        } finally {
            errortKafkaTemplate.flush();
        }
    }
}
