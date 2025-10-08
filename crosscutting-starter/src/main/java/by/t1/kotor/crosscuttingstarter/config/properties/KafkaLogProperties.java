package by.t1.kotor.crosscuttingstarter.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "t1.kafka")
public class KafkaLogProperties {

    private boolean enabled = true;
    private String serviceName = "default-service";
    private String bootstrapServers = "localhost:9092";
    private Map<String, String> topic = new HashMap<>(Map.of("service_logs", "service-logs"));
}
