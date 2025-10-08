package by.t1.kotor.crosscuttingstarter.aop;

import by.t1.kotor.crosscuttingstarter.aop.utils.AspectUtils;
import by.t1.kotor.crosscuttingstarter.dto.MetricLogMessage;
import by.t1.kotor.crosscuttingstarter.kafka.MetricProducer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Map;


@Aspect
@Slf4j
@RequiredArgsConstructor
@Setter
@Getter
public class MetricAspect {

    private final MetricProducer metricProducer;
    private long executionThresholdMs;

    @Around("@annotation(by.t1.kotor.crosscuttingstarter.aop.annotation.Metric)")
    public Object logExecTime(ProceedingJoinPoint pJoinPoint) throws Throwable {
        log.info("Method call: {}", pJoinPoint.getSignature().toShortString());
        long beforeTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = pJoinPoint.proceed();
        } finally {
            long afterTime = System.currentTimeMillis();
            long execTime = afterTime - beforeTime;
            if (execTime > executionThresholdMs) {

                Map<String, Object> argMap = AspectUtils.argsToMap(pJoinPoint.getArgs());

                MetricLogMessage message = MetricLogMessage.builder()
                        .methodSignature(pJoinPoint.getSignature().toString())
                        .methodArgs(argMap)
                        .execTime(execTime)
                        .build();

                metricProducer.sendToTopic(message);
            }
            log.info("Execution time: {} ms", (execTime));
        }

        return result;
    }
}
