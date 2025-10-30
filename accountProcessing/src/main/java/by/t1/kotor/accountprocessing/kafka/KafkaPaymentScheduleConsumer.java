package by.t1.kotor.accountprocessing.kafka;

import by.t1.kotor.accountprocessing.service.PaymentService;
import by.t1.kotor.common.model.dto.PaymentRegistryMessage;
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
public class KafkaPaymentScheduleConsumer {
    private final PaymentService paymentService;

    @KafkaListener(id = "${t1.kafka.consumer.paymentschedule-id}",
            topics = "${t1.kafka.topic.payment-schedule}",
            containerFactory = "paymentScheduleKafkaListenerContainerFactory")
    public void listener(@Payload List<PaymentRegistryMessage> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        log.debug("PaymentSchedule consumer: Обработка новых сообщений");
        try {
            log.info("Topic: {}", topic);
            log.info("Key: {}", key);

            messageList.forEach(message -> {
                log.info("Processing message: {}", message);
                paymentService.createPaymentSchedule(message);
            });

        } finally {
            ack.acknowledge();
        }

        log.debug("PaymentSchedule consumer: записи обработаны");
    }
}
