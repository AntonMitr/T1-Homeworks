package by.t1.kotor.crosscuttingstarter.config;

import by.t1.kotor.crosscuttingstarter.aop.CacheAspect;
import by.t1.kotor.crosscuttingstarter.cache.Cache;
import by.t1.kotor.crosscuttingstarter.config.properties.CacheProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "cache", name = "enabled", havingValue = "true", matchIfMissing = true)
    public Cache cache(CacheProperties properties) {
        Cache cache = new Cache();
        cache.setTtlMs(properties.getTtlMs());
        return cache;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "cache", name = "enabled", havingValue = "true", matchIfMissing = true)
    public CacheAspect cacheAspect(Cache cache) {
        return new CacheAspect(cache);
    }

}
