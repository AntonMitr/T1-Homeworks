package by.t1.kotor.crosscuttingstarter.aop;

import by.t1.kotor.crosscuttingstarter.aop.annotation.LogDatasourceError;
import by.t1.kotor.crosscuttingstarter.aop.utils.AspectUtils;
import by.t1.kotor.crosscuttingstarter.dto.LogErrorMessage;
import by.t1.kotor.crosscuttingstarter.kafka.LogErrorProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

@Aspect
@Slf4j
@RequiredArgsConstructor
public class LogErrorAspect {
    private final LogErrorProducer logErrorProducerProvider;

    @Pointcut("@annotation(by.t1.kotor.crosscuttingstarter.aop.annotation.LogDatasourceError)")
    public void exceptionThrowingMethods() {
    }

    @AfterThrowing(pointcut = "exceptionThrowingMethods()", throwing = "ex")
    public void sendErrorToTopic(JoinPoint joinPoint, Throwable ex) {

        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

            Map<String, Object> argMap = AspectUtils.argsToMap(joinPoint.getArgs());

            LogErrorMessage message = LogErrorMessage.builder()
                    .timestamp(LocalDateTime.now())
                    .methodSignature(methodSignature.toString())
                    .exceptionMessage(ex.getMessage())
                    .stackTrace(Arrays.toString(ex.getStackTrace()))
                    .methodArgs(argMap)
                    .build();

            Method method = methodSignature.getMethod();
            logErrorProducerProvider.sendToTopic(message, method.getAnnotation(LogDatasourceError.class).level());

            log.error("Exception in {}: {}", methodSignature, ex.getMessage(), ex);
        } catch (Exception e) {
            log.error("Failed to log error", e);
        }
    }
}
