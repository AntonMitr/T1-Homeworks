package by.t1.kotor.common.aop;

import by.t1.kotor.common.kafka.HttpRequestLogProducer;
import by.t1.kotor.common.model.dto.HttpRequestLogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LogHttpOutcomeAspect {

    private final HttpRequestLogProducer logProducer;

    @Pointcut("@annotation(by.t1.kotor.common.aop.annotation.HttpOutcomeRequestLog)")
    public void httpMethods() {
    }

    @AfterReturning(pointcut = "httpMethods()", returning = "response")
    public void sendLogToTopic(JoinPoint joinPoint, Object response) {
        String methodSignature = joinPoint.getSignature().toString();

        Object[] args = joinPoint.getArgs();
        Map<String, Object> argMap = new HashMap<>();
        for (Object arg : args) {
            argMap.put(arg.getClass().getName(), arg);
        }

        String uri = getUriFromMethod(joinPoint);

        HttpRequestLogMessage message = HttpRequestLogMessage.builder()
                .timestamp(LocalDateTime.now())
                .methodSignature(methodSignature)
                .uri(uri)
                .methodArgs(argMap)
                .httpBody(response)
                .build();

        logProducer.sendToTopic(message);

    }

    private String getUriFromMethod(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null && requestMapping.value().length > 0) {
            return requestMapping.value()[0];
        }
        return null;
    }
}
