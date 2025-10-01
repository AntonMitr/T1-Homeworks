package by.t1.kotor.common.aop;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogErrorAspect {

    @Pointcut("@annotation(by.t1.kotor.common.aop.annotation.LogDatasourceError)")
    public void exceptionThrowingMethods() {

    }

    @AfterThrowing
    public void sendErrorToTopic() {

    }

}
