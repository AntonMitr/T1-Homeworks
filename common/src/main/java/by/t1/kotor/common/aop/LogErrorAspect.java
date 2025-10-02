package by.t1.kotor.common.aop;

import by.t1.kotor.common.aop.annotation.LogDatasourceError;
import by.t1.kotor.common.kafka.LogErrorProducer;
import by.t1.kotor.common.model.dto.LogErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LogErrorAspect {
    private final LogErrorProducer logErrorProducerProvider;

    @Pointcut("@annotation(by.t1.kotor.common.aop.annotation.LogDatasourceError)")
    public void exceptionThrowingMethods() {
    }

    @AfterThrowing(pointcut = "exceptionThrowingMethods()", throwing = "ex")
    public void sendErrorToTopic(JoinPoint joinPoint, Throwable ex) {

        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

            Object[] args = joinPoint.getArgs();
            Map<String, Object> argMap = new HashMap<>();
            for (Object arg : args) {
                argMap.put(arg.getClass().getName(), arg);
            }

            LogErrorMessage message = LogErrorMessage.builder()
                    .timestamp(LocalDateTime.now())
                    .methodSignature(methodSignature.toString())
                    .exceptionMessage(ex.getMessage())
                    .stackTrace(Arrays.toString(ex.getStackTrace()))
                    .methodArgs(argMap)
                    .build();

            Method method = methodSignature.getMethod();
            logErrorProducerProvider.sendToTopic(message, method.getAnnotation(LogDatasourceError.class).level());

            log.error("Exception in [{}]: {}", methodSignature, ex.getMessage(), ex);
        } catch (Exception e) {
            log.error("Failed to log error", e);
        }
    }
}
