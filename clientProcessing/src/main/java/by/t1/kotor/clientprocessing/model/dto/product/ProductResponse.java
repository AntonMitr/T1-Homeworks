package by.t1.kotor.clientprocessing.model.dto.product;

import by.t1.kotor.clientprocessing.model.enums.KeyEnum;

import java.time.LocalDateTime;

public record ProductResponse(
        String productId,
        String name,
        KeyEnum key,
        LocalDateTime createDate
) {
}