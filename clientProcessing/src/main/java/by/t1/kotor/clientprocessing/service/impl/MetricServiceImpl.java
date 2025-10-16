package by.t1.kotor.clientprocessing.service.impl;

import by.t1.kotor.clientprocessing.service.MetricService;
import by.t1.kotor.clientprocessing.service.metrics.MetricCounter;
import com.google.common.collect.ImmutableList;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static by.t1.kotor.clientprocessing.model.enums.Metrics.CLIENT_CONTROLLER_REQUEST_COUNT;


@Component
public class MetricServiceImpl implements MetricService {

    private final Map<String, MetricCounter> counters;
    private final MeterRegistry meterRegistry;
    private final Tag groupTag = new ImmutableTag("group", "products");

    public MetricServiceImpl(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.counters = new HashMap<>();

        counters.put(CLIENT_CONTROLLER_REQUEST_COUNT.getName(),
                counter(CLIENT_CONTROLLER_REQUEST_COUNT.getName()));
    }

    @Override
    public void incrementByName(String name) {
        counters.computeIfAbsent(name, this::counter).increment();
    }

    private MetricCounter counter(String name) {
        return MetricCounter.create(
                meterRegistry,
                name,
                ImmutableList.of(groupTag),
                b -> {
        });
    }
}
