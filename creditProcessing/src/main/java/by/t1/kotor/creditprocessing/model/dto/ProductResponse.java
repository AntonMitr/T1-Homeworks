package by.t1.kotor.creditprocessing.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductResponse(
        Long id,
        Long clientId,
        Long accountId,
        Long productId,
        BigDecimal interestRate,
        Integer monthCount,
        LocalDate openDate
) {
}
