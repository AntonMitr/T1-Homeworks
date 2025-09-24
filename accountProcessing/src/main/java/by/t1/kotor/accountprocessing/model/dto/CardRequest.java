package by.t1.kotor.accountprocessing.model.dto;

public record CardRequest(
        Long clientId,
        Long productId,
        String productKey,
        String cardType
) {}
