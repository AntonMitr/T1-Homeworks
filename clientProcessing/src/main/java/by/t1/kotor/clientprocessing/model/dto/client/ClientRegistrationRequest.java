package by.t1.kotor.clientprocessing.model.dto.client;

import by.t1.kotor.clientprocessing.model.enums.DocumentTypeEnum;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Set;

@Builder
public record ClientRegistrationRequest(
        String firstName,
        String middleName,
        String lastName,
        LocalDate dateOfBirth,
        DocumentTypeEnum documentType,
        String documentId,
        String documentPrefix,
        String documentSuffix,
        String login,
        String email,
        String password,
        Set<String> roles
) {
}
