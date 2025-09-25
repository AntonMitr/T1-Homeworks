package by.t1.kotor.creditprocessing.kafka;

import by.t1.kotor.creditprocessing.model.dto.CreditProductRequest;
import by.t1.kotor.creditprocessing.service.CreditProductService;
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
public class CreditProductConsumer {

    private final CreditProductService creditProductService;

    @KafkaListener(id = "${t1.kafka.consumer.credit-id}",
            topics = "${t1.kafka.topic.client_credit_products}",
            containerFactory = "creditProductKafkaListenerContainerFactory")
    public void listener(@Payload List<CreditProductRequest> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        log.debug("creditProduct consumer: Обработка новых сообщений");
        try {
            log.info("Topic: {}", topic);
            log.info("Key: {}", key);

            messageList.forEach(message -> {
                log.info("Processing message: {}", message);
/*                creditProductService.(message);*/
            });
        } finally {
            ack.acknowledge();
        }

        log.debug("creditProduct consumer: записи обработаны");
    }
}
