package by.t1.kotor.clientprocessing.model.dto.clientProduct;

import by.t1.kotor.clientprocessing.model.enums.StatusEnum;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ClientProductResponse(
        Long clientId,
        Long productId,
        StatusEnum status,
        LocalDate openDate,
        LocalDate closeDate
) {
}
