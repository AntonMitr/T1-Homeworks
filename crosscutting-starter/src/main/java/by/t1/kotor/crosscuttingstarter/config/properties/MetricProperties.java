package by.t1.kotor.crosscuttingstarter.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("t1.kafka.metric")
@Getter
@Setter
public class MetricProperties {
    private boolean enabled = true;
    private long executionThresholdMs = 1000;
    private String topic = "service_logs";
    private String serviceName = "default-service";
    private String bootstrapServers = "localhost:9092";
}
