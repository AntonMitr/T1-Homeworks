package by.t1.kotor.accountprocessing.kafka;

import by.t1.kotor.accountprocessing.model.dto.AccountRequest;
import by.t1.kotor.accountprocessing.model.dto.AccountResponse;
import by.t1.kotor.accountprocessing.service.AccountService;
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
public class KafkaAccountConsumer {

    private final AccountService accountService;

    @KafkaListener(id = "${t1.kafka.consumer.account-id}",
            topics = "${t1.kafka.topic.client_products}",
            containerFactory = "accountKafkaListenerContainerFactory")
    public void listener(@Payload List<AccountRequest> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        log.debug("Account consumer: Обработка новых сообщений");
        try {
            log.info("Topic: {}", topic);
            log.info("Key: {}", key);

            messageList.forEach(message -> {
                log.info("Processing message: {}", message);
                accountService.createAccount(message);
            });
        } finally {
            ack.acknowledge();
        }

        log.debug("Account consumer: записи обработаны");
    }
}
