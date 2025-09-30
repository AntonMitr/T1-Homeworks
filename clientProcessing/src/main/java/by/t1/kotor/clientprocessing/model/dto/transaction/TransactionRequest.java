package by.t1.kotor.clientprocessing.model.dto.transaction;

import java.math.BigDecimal;

public record TransactionRequest(
        Long clientId,
        Long productId,
        String transactionType,
        String amount
        ) {
}
