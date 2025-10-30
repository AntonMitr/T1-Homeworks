package by.t1.kotor.crosscuttingstarter.aop;

import by.t1.kotor.crosscuttingstarter.aop.utils.AspectUtils;
import by.t1.kotor.crosscuttingstarter.dto.HttpRequestLogMessage;
import by.t1.kotor.crosscuttingstarter.kafka.HttpRequestLogProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LogHttpOutcomeAspect {

    private final HttpRequestLogProducer logProducer;

    @Pointcut("@annotation(by.t1.kotor.crosscuttingstarter.aop.annotation.HttpOutcomeRequestLog)")
    public void httpMethods() {
    }

    @AfterReturning(pointcut = "httpMethods()", returning = "response")
    public void sendLogToTopic(JoinPoint joinPoint, Object response) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        Map<String, Object> argMap = AspectUtils.argsToMap(joinPoint.getArgs());
        String uri = AspectUtils.getUriFromMethod(methodSignature.getMethod());

        HttpRequestLogMessage message = HttpRequestLogMessage.builder()
                .timestamp(LocalDateTime.now())
                .methodSignature(methodSignature.toString())
                .uri(uri)
                .methodArgs(argMap)
                .httpBody(response)
                .build();

        logProducer.sendToTopic(message);
    }
}
