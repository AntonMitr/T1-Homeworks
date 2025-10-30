package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.service.MetricService;
import by.t1.kotor.clientprocessing.service.metrics.MetricCounter;
import com.google.common.collect.ImmutableList;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static by.t1.kotor.clientprocessing.model.enums.Metrics.CLIENT_CONTROLLER_REQUEST_COUNT;


@Component
public class MetricServiceImpl implements MetricService {

    private final Map<String, Counter> counters = new HashMap<>();
    private final MeterRegistry meterRegistry;
    private final Tag groupTag = new ImmutableTag("group", "products");

    public MetricServiceImpl(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        counters.put(CLIENT_CONTROLLER_REQUEST_COUNT.getName(),
                Counter.builder(CLIENT_CONTROLLER_REQUEST_COUNT.getName())
                        .description("Количество созданных продуктов")
                        .tags(ImmutableList.of(groupTag))
                        .register(meterRegistry)
        );
    }

    @Override
    public void incrementByName(String name) {
        counters.computeIfAbsent(name, n ->
                Counter.builder(n)
                        .tags(ImmutableList.of(groupTag))
                        .register(meterRegistry)
        ).increment();
    }
}
