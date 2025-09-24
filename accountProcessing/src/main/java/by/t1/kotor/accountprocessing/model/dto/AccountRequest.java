package by.t1.kotor.accountprocessing.model.dto;

public record AccountRequest (
        Long clientId,
        Long productId,
        String status
){}
