package pl.kurs.zadanie02.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CurrencyCacheService<K, V> implements ICurrencyCache<K, V> {
    private final static Long DEFAULT_CACHE_TIMEOUT = 10_000L;
    private Map<K, CacheValue<V>> cacheValueMap = new ConcurrentHashMap<>();
    private final Long cacheTimeout;


    public CurrencyCacheService() {
        this(DEFAULT_CACHE_TIMEOUT);
    }

    public CurrencyCacheService(Long cacheTimeout) {
        this.cacheTimeout = cacheTimeout;
        clear();
    }


    @Override
    public void clean() {
        for (K expiredKey : getExpiredKeys()) {
            remove(expiredKey);
        }
    }

    @Override
    public void clear() {
        this.cacheValueMap = new ConcurrentHashMap<>();
    }

    @Override
    public boolean containsKey(K key) {
        return cacheValueMap.containsKey(key);
    }

    private Set<K> getExpiredKeys() {
        return cacheValueMap.keySet().parallelStream()
                .filter(this::isExpired)
                .collect(Collectors.toSet());
    }

    private boolean isExpired(K key) {
        LocalDateTime expirationDateTime =
                cacheValueMap.get(key)
                        .getCreatedAt()
                        .plus(cacheTimeout, ChronoUnit.MILLIS);
        return LocalDateTime.now().isAfter(expirationDateTime);

    }

    @Override
    public Optional<V> get(K key) {
        clean();
        return Optional.ofNullable(cacheValueMap.computeIfPresent(key, (k, v) -> isExpired(k) ? null : v))
                .map(CacheValue::getValue);
    }

    @Override
    public void put(K key, V value) {
        cacheValueMap.put(key, createCacheValue(value));
    }

    private CacheValue<V> createCacheValue(V value) {
        LocalDateTime now = LocalDateTime.now();
        return new CacheValue<>() {
            @Override
            public V getValue() {
                return value;
            }

            @Override
            public LocalDateTime getCreatedAt() {
                return now;
            }
        };
    }

    @Override
    public void remove(K key) {
        cacheValueMap.remove(key);
    }

    @Override
    public void compute(K key, V value) {
        cacheValueMap.compute(key, (k, v) -> createCacheValue(value));
    }


    protected interface CacheValue<V> {
        V getValue();

        LocalDateTime getCreatedAt();
    }

}


