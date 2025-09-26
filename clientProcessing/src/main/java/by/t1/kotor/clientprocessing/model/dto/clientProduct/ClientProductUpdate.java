package by.t1.kotor.clientprocessing.model.dto.clientProduct;

import by.t1.kotor.clientprocessing.model.enums.StatusEnum;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ClientProductUpdate(
        @NotNull(message = "closeDate cannot be null")
        LocalDate closeDate,

        @NotNull(message = "status cannot be null")
        StatusEnum status
) {
}
