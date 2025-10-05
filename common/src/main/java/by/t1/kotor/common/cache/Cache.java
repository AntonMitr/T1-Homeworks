package by.t1.kotor.common.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Cache {
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    @Value("${cache.ttl-ms:60000}")
    private long ttlMs;

    public Object get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null || entry.isExpired()) {
            cache.remove(key);
            return null;
        }
        return entry.getValue();
    }

    public void put(String key, Object value) {
        cache.put(key, new CacheEntry(value, ttlMs));
    }

    public void remove(String key) {
        cache.remove(key);
    }

}
