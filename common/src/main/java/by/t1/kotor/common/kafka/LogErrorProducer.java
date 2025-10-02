package by.t1.kotor.common.kafka;

import by.t1.kotor.common.model.dto.LogErrorMessage;
import by.t1.kotor.common.service.ErrorLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogErrorProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ErrorLogService errorLogService;

    @Value("${t1.kafka.topic.service_logs}")
    private String TOPIC;
    @Value("${spring.application.name}")
    private String serviceName;

    public void sendToTopic(LogErrorMessage logErrorMessage, String level) {
        try {
            Message<LogErrorMessage> message = MessageBuilder
                    .withPayload(logErrorMessage)
                    .setHeader(KafkaHeaders.TOPIC, TOPIC)
                    .setHeader(KafkaHeaders.KEY, serviceName)
                    .setHeader("type", level)
                    .build();

            kafkaTemplate.send(message);
            log.info("Save LogErrorMessage to topic {}", message);
        } catch (Exception e) {
            log.error("Error sending message to Kafka, saving to DB", e);
            errorLogService.saveError(logErrorMessage);
        }
    }
}
