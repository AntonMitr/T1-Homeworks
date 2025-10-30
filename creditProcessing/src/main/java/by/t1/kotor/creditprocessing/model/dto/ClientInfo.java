package by.t1.kotor.creditprocessing.model.dto;

import lombok.Builder;

@Builder
public record ClientInfo(
        String firstName,
        String lastName,
        String middleName,
        String documentId
) {
}
