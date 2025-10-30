package by.t1.kotor.common.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionMessage(
        Long clientId,
        Long productId,
        BigDecimal amount,
        String transactionType,
        LocalDateTime timeStamp
) {
}
