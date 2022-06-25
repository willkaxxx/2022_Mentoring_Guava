import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;

public class Cache {
    public static LoadingCache<String, String> getCache(int maxSize) {
        CacheLoader<String, String> loader;
        loader = new CacheLoader<>() {
            @Override
            public String load(String key) throws InterruptedException {
                return key.toUpperCase();
            }
        };

        RemovalListener<String, String> listener;
        listener = n -> System.out.printf("{%s} was evicted\n", n.getValue());

        return CacheBuilder.newBuilder()
                .maximumSize(maxSize) //default eviction policy is LRU
                .removalListener(listener)
                .recordStats()
                .build(loader);
    }
}
