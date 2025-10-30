package by.t1.kotor.crosscuttingstarter.kafka;

import by.t1.kotor.crosscuttingstarter.dto.LogErrorMessage;
import by.t1.kotor.crosscuttingstarter.service.LogErrorService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@RequiredArgsConstructor
@Slf4j
@Setter
public class LogErrorProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectProvider<LogErrorService> logErrorServiceProvider;


    private String TOPIC;
    private String KEY;

    public void sendToTopic(LogErrorMessage logErrorMessage, String level) {
        try {
            Message<LogErrorMessage> message = MessageBuilder
                    .withPayload(logErrorMessage)
                    .setHeader(KafkaHeaders.TOPIC, TOPIC)
                    .setHeader(KafkaHeaders.KEY, KEY)
                    .setHeader("type", level)
                    .build();

            kafkaTemplate.send(message);
            log.info("Save LogErrorMessage to topic {}", message);
        } catch (Exception e) {
            log.error("Error sending message to Kafka, saving to DB", e);
            logErrorServiceProvider.ifAvailable(svc -> svc.saveError(logErrorMessage));
        }
    }
}
