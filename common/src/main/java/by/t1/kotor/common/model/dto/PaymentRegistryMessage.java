package by.t1.kotor.common.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentRegistryMessage(
        Long accountId,
        BigDecimal interestRate,
        Integer monthCount,
        BigDecimal amount,
        LocalDate paymentExpirationDate
) {
}
