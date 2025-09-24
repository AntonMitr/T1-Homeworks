package by.t1.kotor.clientprocessing.model.dto.clientProduct;

public record ClientProductMessage(
        Long clientId,
        Long productId,
        String status
) {}
