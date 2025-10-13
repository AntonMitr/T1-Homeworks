package by.t1.kotor.common.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
public record ClientProductMessage(
        Long clientId,
        Long productId,
        BigDecimal creditAmount,
        BigDecimal interestRate,
        Integer monthCount
) {
}
