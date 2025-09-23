package by.t1.kotor.clientprocessing.model.dto;

public record ClientResponse (
        String clientId,
        String firstName,
        String lastName,
        String login,
        String email
) {}
