package by.t1.kotor.common.aop.kafka;

import by.t1.kotor.common.model.dto.LogErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogErrorProducer {

    private final KafkaTemplate<String, LogErrorMessage> kafkaTemplate;
    @Value("${kafka.topic.service_logs}")
    private String TOPIC;
    @Value("${spring.application.name}")
    private String KEY;

    public void sendToTopic(LogErrorMessage message) {
        try {
            kafkaTemplate.send(
                    TOPIC,
                    0,
                    LocalDateTime.now().toEpochSecond(ZoneOffset.of("+03:00")),
                    KEY,
                    message
            ).get();
        } catch (Exception e) {
            log.error("Error sending message", e);
        } finally {
            kafkaTemplate.flush();
        }
    }
}
