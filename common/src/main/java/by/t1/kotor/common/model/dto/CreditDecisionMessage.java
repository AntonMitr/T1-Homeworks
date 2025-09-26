package by.t1.kotor.common.model.dto;

public record CreditDecisionMessage(
        Long clientId,
        Long productId,
        boolean approved,
        String reason
) {
}
