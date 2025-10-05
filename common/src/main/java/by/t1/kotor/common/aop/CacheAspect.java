package by.t1.kotor.common.aop;

import by.t1.kotor.common.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class CacheAspect {
    private final Cache cache;

    @Around("@annotation(by.t1.kotor.common.aop.annotation.Cached)")
    public Object cacheMethod(ProceedingJoinPoint pJoinPoint) throws Throwable {
        Object[] args = pJoinPoint.getArgs();

        String key = pJoinPoint.getSignature().toShortString() + Arrays.deepHashCode(args);
        log.info("Проверка кэша по ключу: {}", key);

        Object cachedResult = cache.get(key);
        if (cachedResult != null) {
            log.info("Возврат из кэша: {}", pJoinPoint.getSignature().toShortString());
            return cachedResult;
        }

        Object result = pJoinPoint.proceed();

        cache.put(key, result);
        log.info("Добавлено в кэш: {}", pJoinPoint.getSignature().toShortString());

        return result;
    }
}
