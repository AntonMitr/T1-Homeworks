package by.t1.kotor.clientprocessing.service;

import by.t1.kotor.clientprocessing.model.dto.card.CardRequest;

public interface CardService {
    void sendCardCreateMessage(CardRequest request);
}
