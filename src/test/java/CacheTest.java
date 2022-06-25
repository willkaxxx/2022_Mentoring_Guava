import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.ExecutionException;

class CacheTest {

    @Test
    void LRUStrategyTest() {
        var cache = Cache.getCache(3);

        cache.getUnchecked("A");
        cache.getUnchecked("B");
        cache.getUnchecked("A");
        cache.getUnchecked("D");
        cache.getUnchecked("E");
        cache.getUnchecked("A");
        cache.getUnchecked("F");

        Assertions.assertTrue(cache.asMap().containsKey("E"));
        Assertions.assertTrue(cache.asMap().containsKey("A"));
        Assertions.assertTrue(cache.asMap().containsKey("F"));
    }

    @Test
    void testGetStats() {
        var cache = Cache.getCache(3);

        cache.getUnchecked("A");
        cache.getUnchecked("B");
        cache.getUnchecked("A");
        cache.getUnchecked("D");
        cache.getUnchecked("E");
        cache.getUnchecked("A");
        cache.getUnchecked("F");

        Assertions.assertEquals(2, cache.stats().evictionCount());
        Assertions.assertTrue(cache.stats().averageLoadPenalty() > 1000);
    }

    @Test
    void loadTest() throws ExecutionException {
        var cache = Cache.getCache(100_000);

        PrintStream out = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int arg0) throws IOException {

            }
        }));

        Random random = new Random();

        for (int i = 0; i < 10_000_000; i++) {
            String someStr = Integer.toString(random.nextInt(1_000_000));
            cache.getUnchecked(someStr);

            while (random.nextBoolean()) {
                cache.get(someStr);
            }
        }

        System.setOut(out);

        System.out.printf("Avg put time: %sns%n", cache.stats().averageLoadPenalty());
        System.out.printf("Eviction count: %s%n", cache.stats().evictionCount());
    }
}