package by.t1.kotor.common.model.dto;

public record ClientProductMessage(
        Long clientId,
        Long productId,
        String status
) {}
