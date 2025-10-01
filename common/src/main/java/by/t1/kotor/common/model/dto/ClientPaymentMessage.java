package by.t1.kotor.common.model.dto;

import java.math.BigDecimal;

public record ClientPaymentMessage(
        Long clientId,
        Long productId,
        BigDecimal amount,
        Long accountId

) {
}
