package by.t1.kotor.common.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record HttpRequestLogMessage(
        LocalDateTime timestamp,
        String methodSignature,
        String uri,
        Map<String, Object> methodArgs,
        Object httpBody
) {
}
