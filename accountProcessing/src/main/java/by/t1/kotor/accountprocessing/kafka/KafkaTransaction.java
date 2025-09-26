package by.t1.kotor.accountprocessing.kafka;

import by.t1.kotor.common.model.dto.TransactionMessage;
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
public class KafkaTransaction {
    @KafkaListener(id = "${t1.kafka.consumer.transaction-id}",
            topics = "${t1.kafka.topic.client_transactions}",
            containerFactory = "transactionKafkaListenerContainerFactory")
    public void listener(@Payload List<TransactionMessage> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        log.debug("Transaction consumer: Обработка новых сообщений");
        try {
            log.info("Topic: {}", topic);
            log.info("Key: {}", key);

            messageList.forEach(message -> {
                log.info("Processing message: {}", message);
                //todo
            });

        } finally {
            ack.acknowledge();
        }

        log.debug("Transaction consumer: записи обработаны");
    }
}
