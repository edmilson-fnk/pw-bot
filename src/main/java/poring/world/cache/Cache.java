package poring.world.cache;

import java.util.HashMap;
import java.util.Map;

public class Cache extends HashMap<String, String> {

    private static final float TTL = 10 * 60 * 1000; // 10 minutes to expire data
    private final Map<Object, Long> creationTime = new HashMap<>();

    @Override
    public String put(String key, String value) {
        this.creationTime.put(key, System.currentTimeMillis());
        return super.put(key, value);
    }

    @Override
    public boolean containsKey(Object key) {
        if (isExpired(key)) {
            return false;
        } else {
            return super.containsKey(key);
        }
    }

    @Override
    public String get(Object key) {
        if (isExpired(key)) {
            return null;
        } else {
            return super.get(key);
        }
    }

    public boolean isExpired(Object key) {
        return System.currentTimeMillis() - this.creationTime.getOrDefault(key, 0L) > TTL;
    }
}
