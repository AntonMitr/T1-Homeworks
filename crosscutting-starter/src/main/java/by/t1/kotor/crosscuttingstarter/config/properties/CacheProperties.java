package by.t1.kotor.crosscuttingstarter.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cache")
@Getter
@Setter
public class CacheProperties {
    private boolean enabled = true;
    private long ttlMs = 60000;
}
