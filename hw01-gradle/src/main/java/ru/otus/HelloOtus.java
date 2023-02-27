package ru.otus;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class HelloOtus {
    private static final LoadingCache<String, String> GUAVA_CACHE = CacheBuilder.newBuilder()
            .build(CacheLoader.from((String key) -> "it works with %s!".formatted(key)));

    public static void main(String... args) {
        String result = GUAVA_CACHE.getUnchecked("any key");
        System.out.println(result);
    }
}
