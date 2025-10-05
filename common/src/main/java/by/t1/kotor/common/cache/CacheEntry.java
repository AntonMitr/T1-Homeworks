package by.t1.kotor.common.cache;

import lombok.Getter;

public class CacheEntry {
    @Getter
    private final Object value;
    private final long expiryTime;

    public CacheEntry(Object value, long ttlMs) {
        this.value = value;
        this.expiryTime = System.currentTimeMillis() + ttlMs;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}
