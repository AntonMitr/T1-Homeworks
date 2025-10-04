package by.t1.kotor.common.model.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record MetricLogMessage(
        String methodSignature,
        Map<String, Object> methodArgs,
        long execTime
) {
}
