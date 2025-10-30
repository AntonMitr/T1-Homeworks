package by.t1.kotor.crosscuttingstarter.kafka;

import by.t1.kotor.crosscuttingstarter.dto.MetricLogMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@RequiredArgsConstructor
@Slf4j
@Setter
public class MetricProducer {
    private final KafkaTemplate<String, Object> metricKafkaTemplate;

    private String TOPIC;
    private String KEY;

    public void sendToTopic(MetricLogMessage logMessage) {
        try {
            Message<MetricLogMessage> message = MessageBuilder
                    .withPayload(logMessage)
                    .setHeader(KafkaHeaders.TOPIC, TOPIC)
                    .setHeader(KafkaHeaders.KEY, KEY)
                    .setHeader("type", "WARNING")
                    .build();

            metricKafkaTemplate.send(message);
            log.info("Metric sent: {}", message);
        } catch (Exception e) {
            log.error("Error sending message", e);
        }
    }
}
