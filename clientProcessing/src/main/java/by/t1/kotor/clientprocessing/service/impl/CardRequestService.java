package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.kafka.KafkaProducer;
import by.t1.kotor.clientprocessing.mapper.CardMapper;
import by.t1.kotor.clientprocessing.model.dto.card.CardRequest;
import by.t1.kotor.common.model.dto.CardMessage;
import by.t1.kotor.crosscuttingstarter.aop.annotation.LogDatasourceError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardRequestService {

    private final KafkaProducer<CardMessage> kafkaProducer;
    private final CardMapper cardMapper;

    @Value("${t1.kafka.topic.client_cards}")
    private String TOPIC;

    @LogDatasourceError
    public void sendCardCreateMessage(CardRequest request) {
        log.info("Preparing to send card create message: {}", request);

        CardMessage message = cardMapper.toMessage(request);
        log.debug("Mapped CardRequest to CardMessage: {}", message);

        kafkaProducer.sendTo(TOPIC, message);
        log.info("Sent CardMessage to topic {} successfully: {}", TOPIC, message);
    }
}
