package by.t1.kotor.clientprocessing.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
@Slf4j
public class KafkaProducer<T> {

    private final KafkaTemplate<String, T> objectKafkaTemplate;

    public KafkaProducer(@Qualifier("objectKafkaTemplate") KafkaTemplate<String, T> objectKafkaTemplate) {
        this.objectKafkaTemplate = objectKafkaTemplate;
    }

    public void sendDefault(T message) {
        try {
            objectKafkaTemplate.sendDefault(UUID.randomUUID().toString(), message).get();
        } catch (Exception e) {
            log.error("Error sending message", e);
        } finally {
            objectKafkaTemplate.flush();
        }
    }

    public void sendTo(String topic, T message) {
        try {
            objectKafkaTemplate.send(
                    topic,
                    0,
                    LocalDateTime.now().toEpochSecond(ZoneOffset.of("+03:00")),
                    UUID.randomUUID().toString(),
                    message
            ).get();
        } catch (Exception e) {
            log.error("Error sending message", e);
        } finally {
            objectKafkaTemplate.flush();
        }
    }

}
