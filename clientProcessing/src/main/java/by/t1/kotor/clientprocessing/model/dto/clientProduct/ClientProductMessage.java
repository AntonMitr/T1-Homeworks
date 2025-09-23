package by.t1.kotor.clientprocessing.model.dto.clientProduct;

import java.time.LocalDate;

public record ClientProductMessage(
        Long id,
        Long clientId,
        Long productId,
        String status,
        LocalDate openDate
) {}
