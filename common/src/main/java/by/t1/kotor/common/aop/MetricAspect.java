package by.t1.kotor.common.aop;

import by.t1.kotor.common.kafka.MetricProducer;
import by.t1.kotor.common.model.dto.MetricLogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
public class MetricAspect {

    private final MetricProducer metricProducer;

    @Value("${metrics.execution-threshold-ms}")
    private long executionThresholdMs;

    @Around("@annotation(by.t1.kotor.common.aop.annotation.Metric)")
    public Object logExecTime(ProceedingJoinPoint pJoinPoint) throws Throwable {
        log.info("Вызов метода: {}", pJoinPoint.getSignature().toShortString());
        long beforeTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = pJoinPoint.proceed();
        } finally {
            long afterTime = System.currentTimeMillis();
            long execTime = afterTime - beforeTime;
            if (execTime > executionThresholdMs) {

                Object[] args = pJoinPoint.getArgs();
                Map<String, Object> argMap = new HashMap<>();
                for (Object arg : args) {
                    argMap.put(arg.getClass().getName(), arg);
                }

                MetricLogMessage message = MetricLogMessage.builder()
                        .methodSignature(pJoinPoint.getSignature().toString())
                        .methodArgs(argMap)
                        .execTime(execTime)
                        .build();

                metricProducer.sendToTopic(message);
            }
            log.info("Время исполнения: {} ms", (execTime));
        }

        return result;
    }
}
