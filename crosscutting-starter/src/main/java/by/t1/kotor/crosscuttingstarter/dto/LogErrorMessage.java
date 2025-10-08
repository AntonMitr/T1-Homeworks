package by.t1.kotor.crosscuttingstarter.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record LogErrorMessage(
        LocalDateTime timestamp,
        String methodSignature,
        String stackTrace,
        String exceptionMessage,
        Map<String, Object> methodArgs
) {
}
