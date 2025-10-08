package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.kafka.KafkaProducer;
import by.t1.kotor.clientprocessing.mapper.TransactionMapper;
import by.t1.kotor.clientprocessing.model.dto.transaction.TransactionRequest;
import by.t1.kotor.common.model.dto.TransactionMessage;
import by.t1.kotor.crosscuttingstarter.aop.annotation.LogDatasourceError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionRequestService {

    private final KafkaProducer<TransactionMessage> kafkaProducer;
    private final TransactionMapper transactionMapper;

    @Value("${t1.kafka.topic.client_transactions}")
    private String TOPIC;

    @LogDatasourceError
    public void sendCardCreateMessage(TransactionRequest request) {
        log.info("Preparing to send transaction create message: {}", request);

        TransactionMessage message = transactionMapper.toMessage(request);
        log.debug("Mapped TransactionRequest to TransactionMessage: {}", message);

        kafkaProducer.sendTo(TOPIC, message);
        log.info("Sent TransactionMessage to topic {} successfully: {}", TOPIC, message);
    }
}
