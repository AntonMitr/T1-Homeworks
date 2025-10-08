package by.t1.kotor.crosscuttingstarter.aop.utils;

import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AspectUtils {
    public static Map<String, Object> argsToMap(Object[] args) {
        Map<String, Object> argMap = new HashMap<>();
        if (args != null) {
            for (Object arg : args) {
                if (arg != null) {
                    argMap.put(arg.getClass().getName(), arg);
                }
            }
        }
        return argMap;
    }

    public static String getUriFromMethod(Method method) {
        if (method == null) return null;
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null && requestMapping.value().length > 0) {
            return requestMapping.value()[0];
        }
        return null;
    }
}
