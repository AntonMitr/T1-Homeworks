package by.t1.kotor.clientprocessing.model.dto.clientProduct;

import by.t1.kotor.clientprocessing.model.enums.StatusEnum;

import java.time.LocalDate;

public record ClientProductUpdate(
        LocalDate closeDate,
        StatusEnum status
) {}
