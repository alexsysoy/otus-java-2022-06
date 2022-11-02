package cachehw;


import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы
    private final Set<HwListener<K, V>> listeners;
    private final WeakHashMap<K, WeakReference<V>> cache;

    public MyCache() {
        listeners = new HashSet<>();
        cache = new WeakHashMap<>();
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, new WeakReference<>(value));
        notifyAll(key, value, "PUT cashSize:" + cache.size());
    }

    @Override
    public void remove(K key) {
        if (cache.containsKey(key)) {
            var value = cache.remove(key);
            notifyAll(key, value.get(), "REMOVE cashSize:" + cache.size());
        }
    }

    @Override
    public V get(K key) {
        if (cache.containsKey(key)) {
            var value = cache.get(key);
            notifyAll(key, value.get(), "GET cashSize:" + cache.size());
            return value.get();
        }
        return null;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyAll(K key, V value, String action) {
        listeners.forEach(listener -> listener.notify(key, value, action));
    }
}
