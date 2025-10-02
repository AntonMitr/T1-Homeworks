package by.t1.kotor.clientprocessing.model.dto.transaction;

public record TransactionRequest(
        Long clientId,
        Long productId,
        String transactionType,
        String amount
) {
}
