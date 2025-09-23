package by.t1.kotor.clientprocessing.model.dto.clientProduct;

import by.t1.kotor.clientprocessing.model.enums.StatusEnum;

import java.time.LocalDate;

public record ClientProductResponse(
        Long clientId,
        Long productId,
        StatusEnum status,
        LocalDate openDate,
        LocalDate closeDate
) {}
