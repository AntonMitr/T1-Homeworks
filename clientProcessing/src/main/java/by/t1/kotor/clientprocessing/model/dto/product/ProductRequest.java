package by.t1.kotor.clientprocessing.model.dto.product;

import by.t1.kotor.clientprocessing.model.enums.KeyEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
        @NotBlank(message = "name cannot be blank")
        String name,

        @NotNull(message = "key cannot be null")
        KeyEnum key
) {
}
