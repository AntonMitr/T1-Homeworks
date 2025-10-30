package by.t1.kotor.crosscuttingstarter.cache;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class Cache {
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

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
