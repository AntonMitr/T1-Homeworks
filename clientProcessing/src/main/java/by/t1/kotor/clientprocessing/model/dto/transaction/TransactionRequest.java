package by.t1.kotor.clientprocessing.model.dto.transaction;

import lombok.Builder;

@Builder
public record TransactionRequest(
        Long clientId,
        Long productId,
        String transactionType,
        String amount
) {
}
