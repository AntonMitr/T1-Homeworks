package by.t1.kotor.clientprocessing.model.dto.clientProduct;


import by.t1.kotor.clientprocessing.model.enums.StatusEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record ClientProductRequest(
        @NotNull(message = "clientId cannot be null")
        Long clientId,

        @NotNull(message = "productId cannot be null")
        Long productId,

        @NotNull(message = "status cannot be null")
        StatusEnum status,

        @Pattern(regexp = "\\d+(\\.\\d+)?", message = "creditAmount must be a number")
        String creditAmount,

        @Pattern(regexp = "\\d+(\\.\\d+)?", message = "interestRate must be a number")
        String interestRate,

        @Positive(message = "monthCount must be positive")
        Integer monthCount
) {
}
