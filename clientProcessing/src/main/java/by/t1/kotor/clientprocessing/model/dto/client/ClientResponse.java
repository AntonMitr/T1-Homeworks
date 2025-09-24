package by.t1.kotor.clientprocessing.model.dto.client;

public record ClientResponse (
        String clientId,
        String firstName,
        String lastName,
        String login,
        String email
) {}
