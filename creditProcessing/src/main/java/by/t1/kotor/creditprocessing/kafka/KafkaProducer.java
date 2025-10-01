package by.t1.kotor.creditprocessing.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer<T> {

    private final KafkaTemplate<String, T> kafkaTemplate;

    public void sendDefault(T message) {
        try {
            kafkaTemplate.sendDefault(UUID.randomUUID().toString(), message).get();
        } catch (Exception e) {
            log.error("Error sending message", e);
        } finally {
            kafkaTemplate.flush();
        }
    }

    public void sendTo(String topic, T message) {
        try {
            kafkaTemplate.send(
                    topic,
                    0,
                    LocalDateTime.now().toEpochSecond(ZoneOffset.of("+03:00")),
                    UUID.randomUUID().toString(),
                    message
            ).get();
        } catch (Exception e) {
            log.error("Error sending message", e);
        } finally {
            kafkaTemplate.flush();
        }
    }

}
