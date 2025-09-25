package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.kafka.KafkaProducer;
import by.t1.kotor.clientprocessing.mapper.CardMapper;
import by.t1.kotor.common.model.dto.CardMessage;
import by.t1.kotor.clientprocessing.model.dto.card.CardRequest;
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

    public void sendCardCreateMessage(CardRequest request) {
        CardMessage message = cardMapper.toMessage(request);

        kafkaProducer.sendTo(TOPIC, message);
        log.info("Sent cardMessage to topic {}: {}", TOPIC, message);
    }

}
