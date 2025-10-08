package by.t1.kotor.crosscuttingstarter.config;

import by.t1.kotor.crosscuttingstarter.aop.MetricAspect;
import by.t1.kotor.crosscuttingstarter.config.properties.KafkaLogProperties;
import by.t1.kotor.crosscuttingstarter.config.properties.MetricProperties;
import by.t1.kotor.crosscuttingstarter.kafka.MetricProducer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
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
@EnableConfigurationProperties(MetricProperties.class)
@ConditionalOnProperty(prefix = "t1.kafka.metric", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class MetricAutoConfiguration {

    private final MetricProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public ProducerFactory<String, Object> metricProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    @ConditionalOnMissingBean
    public KafkaTemplate<String, Object> metricKafkaTemplate(ProducerFactory<String, Object> factory) {
        return new KafkaTemplate<>(factory);
    }

    @Bean
    @ConditionalOnMissingBean
    public MetricProducer metricProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        MetricProducer producer = new MetricProducer(kafkaTemplate);
        producer.setTOPIC(properties.getTopic());
        producer.setKEY(properties.getServiceName());
        return producer;
    }

    @Bean
    @ConditionalOnMissingBean
    public MetricAspect metricAspect(MetricProducer metricProducer, MetricProperties properties) {
        MetricAspect aspect = new MetricAspect(metricProducer);
        aspect.setExecutionThresholdMs(properties.getExecutionThresholdMs());
        return aspect;
    }
}
