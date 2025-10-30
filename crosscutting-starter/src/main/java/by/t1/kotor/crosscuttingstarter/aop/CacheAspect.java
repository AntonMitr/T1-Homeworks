package by.t1.kotor.crosscuttingstarter.aop;

import by.t1.kotor.crosscuttingstarter.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;

@Aspect
@Slf4j
@RequiredArgsConstructor
public class CacheAspect {
    private final Cache cache;

    @Around("@annotation(by.t1.kotor.crosscuttingstarter.aop.annotation.Cached)")
    public Object cacheMethod(ProceedingJoinPoint pJoinPoint) throws Throwable {
        Object[] args = pJoinPoint.getArgs();

        String key = pJoinPoint.getSignature().toShortString() + Arrays.deepHashCode(args);
        log.info("Checking cache by key: {}", key);

        Object cachedResult = cache.get(key);
        if (cachedResult != null) {
            log.info("Return from cache: {}", pJoinPoint.getSignature().toShortString());
            return cachedResult;
        }

        Object result = pJoinPoint.proceed();

        cache.put(key, result);
        log.info("Added to cache: {}", pJoinPoint.getSignature().toShortString());

        return result;
    }
}
