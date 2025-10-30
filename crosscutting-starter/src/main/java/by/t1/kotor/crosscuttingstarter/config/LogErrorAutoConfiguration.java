package by.t1.kotor.crosscuttingstarter.config;

import by.t1.kotor.crosscuttingstarter.aop.LogErrorAspect;
import by.t1.kotor.crosscuttingstarter.config.properties.KafkaLogProperties;
import by.t1.kotor.crosscuttingstarter.dto.LogErrorMessage;
import by.t1.kotor.crosscuttingstarter.kafka.LogErrorProducer;
import by.t1.kotor.crosscuttingstarter.service.LogErrorService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@AutoConfiguration
@ConditionalOnClass(KafkaTemplate.class)
@EnableConfigurationProperties(KafkaLogProperties.class)
@ConditionalOnProperty(prefix = "t1.kafka", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class LogErrorAutoConfiguration {

    private final KafkaLogProperties properties;
    private final ObjectProvider<LogErrorService> logErrorServiceProvider;

    @Bean
    @ConditionalOnMissingBean
    public LogErrorAspect logErrorAspect(LogErrorProducer producer) {
        return new LogErrorAspect(producer);
    }

    @Bean
    @ConditionalOnMissingBean
    public KafkaTemplate<String, Object> logErrorKafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public LogErrorProducer logErrorProducer(KafkaTemplate template) {
        LogErrorProducer producer = new LogErrorProducer(template, logErrorServiceProvider);
        producer.setTOPIC(properties.getTopic().get("service_logs"));
        producer.setKEY(properties.getServiceName());
        return producer;
    }

    @Bean
    @ConditionalOnMissingBean
    public ProducerFactory<String, Object> logErrorProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return new DefaultKafkaProducerFactory<>(props);
    }


}
