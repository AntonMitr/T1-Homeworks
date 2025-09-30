package by.t1.kotor.creditprocessing.kafka;

import by.t1.kotor.common.model.dto.ClientProductMessage;
import by.t1.kotor.creditprocessing.service.impl.CreditDecisionService;
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
public class KafkaProductConsumer {
    private final CreditDecisionService creditDecisionService;

    @KafkaListener(id = "${t1.kafka.consumer.product-id}",
            topics = "${t1.kafka.topic.client_credit_products}",
            containerFactory = "productKafkaListenerContainerFactory")
    public void listener(@Payload List<ClientProductMessage> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        log.debug("Product consumer: Обработка новых сообщений");
        try {
            log.info("Topic: {}", topic);
            log.info("Key: {}", key);

            messageList.forEach(message -> {
                log.info("Processing message: {}", message);
                creditDecisionService.decideCredit(message);
            });
        } finally {
            ack.acknowledge();
        }

        log.debug("Product consumer: записи обработаны");
    }
}
