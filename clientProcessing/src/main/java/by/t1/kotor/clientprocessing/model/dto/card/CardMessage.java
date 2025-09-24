package by.t1.kotor.clientprocessing.model.dto.card;

public record CardMessage(
        Long clientId,
        Long productId,
        String productKey,
        String cardType
) {}
