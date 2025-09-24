package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.kafka.KafkaProducer;
import by.t1.kotor.clientprocessing.mapper.CardMapper;
import by.t1.kotor.clientprocessing.model.dto.card.CardMessage;
import by.t1.kotor.clientprocessing.model.dto.card.CardRequest;
import by.t1.kotor.clientprocessing.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {

    private final KafkaProducer<CardMessage> kafkaProducer;
    private final CardMapper cardMapper;

    private static final String TOPIC = "client_cards";

    public void sendCardCreateMessage(CardRequest request) {
        CardMessage message = cardMapper.toMessage(request);

        kafkaProducer.sendTo(TOPIC, message);
        log.info("Sent cardMessage to topic {}: {}", TOPIC, message);
    }

}
