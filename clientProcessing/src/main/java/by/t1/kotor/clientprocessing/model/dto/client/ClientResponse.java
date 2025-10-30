package by.t1.kotor.clientprocessing.model.dto.client;

import by.t1.kotor.clientprocessing.model.enums.DocumentTypeEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
public record ClientResponse(
        @NotBlank(message = "firstName cannot be blank")
        String firstName,

        String middleName,

        @NotBlank(message = "lastName cannot be blank")
        String lastName,

        @NotNull(message = "dateOfBirth cannot be null")
        LocalDate dateOfBirth,

        @NotNull(message = "documentType cannot be null")
        DocumentTypeEnum documentType,

        @NotBlank(message = "documentId cannot be blank")
        String documentId,

        String documentPrefix,

        String documentSuffix,

        @NotBlank(message = "login cannot be blank")
        String login,

        @Email(message = "email must be a valid email")
        String email,

        @NotBlank(message = "password cannot be blank")
        String password
) {
}
