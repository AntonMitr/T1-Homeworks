package by.t1.kotor.crosscuttingstarter.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record MetricLogMessage(
        String methodSignature,
        Map<String, Object> methodArgs,
        long execTime
) {
}
