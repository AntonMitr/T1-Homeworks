package by.t1.kotor.clientprocessing.model.dto.clientProduct;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record ClientProductRequest(
        @NotNull(message = "clientId cannot be null")
        Long clientId,

        @NotNull(message = "productId cannot be null")
        Long productId,

        @Pattern(regexp = "\\d+(\\.\\d+)?", message = "creditAmount must be a number")
        String creditAmount,

        @Pattern(regexp = "\\d+(\\.\\d+)?", message = "interestRate must be a number")
        String interestRate,

        @Positive(message = "monthCount must be positive")
        Integer monthCount
) {
}
