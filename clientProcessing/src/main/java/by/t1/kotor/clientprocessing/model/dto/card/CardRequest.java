package by.t1.kotor.clientprocessing.model.dto.card;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CardRequest(
        @NotNull(message = "clientId cannot be null")
        Long clientId,

        @NotNull(message = "productId cannot be null")
        Long productId,

        String cardType
) {
}
