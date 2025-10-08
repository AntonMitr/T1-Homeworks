package by.t1.kotor.crosscuttingstarter.kafka;

import by.t1.kotor.crosscuttingstarter.dto.HttpRequestLogMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Setter
public class HttpRequestLogProducer {
    private final KafkaTemplate<String, Object> errortKafkaTemplate;

    private String TOPIC;
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
            log.info("Http log sent: {}", message);
        } catch (Exception e) {
            log.error("Error sending message", e);
        } finally {
            errortKafkaTemplate.flush();
        }
    }
}
