package by.t1.kotor.common.model.dto;

import java.math.BigDecimal;

public record ClientProductMessage(
        Long clientId,
        Long productId,
        String status,
        BigDecimal creditAmount,
        BigDecimal interestRate,
        Integer monthCount
) {
}
