package pl.kurs.zadanie02.services;

import java.util.Optional;
import java.util.function.Function;

public interface ICurrencyCache<K, V> {
    void clean();

    void clear();

    boolean containsKey(K key);

    Optional<V> get(K key);

    void put(K key, V value);

    void remove(K key);

    void compute(K key, V value);

    V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction);

}
