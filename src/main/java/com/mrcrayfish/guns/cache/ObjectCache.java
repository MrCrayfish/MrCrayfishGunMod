package com.mrcrayfish.guns.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public final class ObjectCache
{
    private static final Map<String, ObjectCache> INSTANCES = new HashMap<>();

    public static ObjectCache getInstance(String key)
    {
        return INSTANCES.computeIfAbsent(key, s -> new ObjectCache());
    }

    private final Map<Object, Object> cache = new HashMap<>();

    private ObjectCache() {}

    @SuppressWarnings("unchecked")
    public <T> T store(Object key, Supplier<T> supplier)
    {
        T cacheValue = (T) this.cache.get(key);
        if(cacheValue != null)
        {
            return cacheValue;
        }
        T value = supplier.get();
        Objects.requireNonNull(value);
        this.cache.put(key, value);
        return value;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(Object key)
    {
        return Optional.ofNullable((T) this.cache.get(key));
    }

    public void reset()
    {
        this.cache.clear();
    }
}
