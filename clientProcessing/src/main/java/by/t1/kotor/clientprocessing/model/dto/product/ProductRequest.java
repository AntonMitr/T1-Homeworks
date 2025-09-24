package by.t1.kotor.clientprocessing.model.dto.product;

import by.t1.kotor.clientprocessing.model.enums.KeyEnum;

public record ProductRequest (
        String name,
        KeyEnum key
) {}
