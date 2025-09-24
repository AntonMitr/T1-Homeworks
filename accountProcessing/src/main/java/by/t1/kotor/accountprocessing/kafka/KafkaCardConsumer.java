package by.t1.kotor.accountprocessing.kafka;

import by.t1.kotor.accountprocessing.model.dto.CardRequest;
import by.t1.kotor.accountprocessing.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaCardConsumer {

    private final CardService cardService;

    @KafkaListener(id = "${t1.kafka.consumer.card-id}",
            topics = "${t1.kafka.topic.client_cards}",
            containerFactory = "cardKafkaListenerContainerFactory")
    public void listener(@Payload List<CardRequest> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        log.debug("Card consumer: Обработка новых сообщений");
        try {
            log.info("Topic: {}", topic);
            log.info("Key: {}", key);

            messageList.forEach(message -> {
                log.info("Processing message: {}", message);
                cardService.create(message);
            });

        } finally {
            ack.acknowledge();
        }

        log.debug("Card consumer: записи обработаны");
    }
}
