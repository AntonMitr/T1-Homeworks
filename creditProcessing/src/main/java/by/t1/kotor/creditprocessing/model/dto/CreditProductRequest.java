package by.t1.kotor.creditprocessing.model.dto;

public record CreditProductRequest(
        Long clientId,
        Long productId,
        String status
) {}
