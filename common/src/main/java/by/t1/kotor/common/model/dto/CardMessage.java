package by.t1.kotor.common.model.dto;

public record CardMessage(
        Long clientId,
        Long productId,
        String productKey,
        String cardType
) {}
