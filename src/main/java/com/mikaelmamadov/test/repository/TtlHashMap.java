package com.mikaelmamadov.test.repository;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

@Component
public class TtlHashMap<K, V> implements Map<K, V>, Serializable {
    private final Map<K, V> store;
    private final Map<K, Long> createdAt;
    private final Map<K, Long> timeout;
    private Long defaultTime = 0L;
    private static final long serialVersionUID = 6529685098267757690L;


    public TtlHashMap(){
        store = new HashMap<>();
        createdAt = new HashMap<>();
        timeout = new HashMap<>();
        defaultTime = 15 * 60 * 1000L;
    }

    @Override
    public V get(Object key) {
        V value = store.get(key);

        if(value != null && expired(key, value)){
            store.remove(key);
            createdAt.remove(key);
            timeout.remove(key);
            return null;
        }
        return value;
    }



    @Override
    public V put(K key, V value) {
        createdAt.put(key, System.currentTimeMillis());
        timeout.put(key, defaultTime);
        return store.put(key, value);
    }

    public V put(K key, V value, Long ttlInMinutes) {
        Long ttlToMillis = ttlInMinutes * 60 * 1000;
        createdAt.put(key, System.currentTimeMillis());
        timeout.put(key, ttlToMillis);
        return store.put(key, value);
    }

    @Override
    public V remove(Object key) {
        createdAt.remove(key);
        timeout.remove(key);
        return store.remove(key);
    }

    private boolean expired(Object key, V value){
        return (System.currentTimeMillis() - createdAt.get(key)) > timeout.get(key);
    }

    @Override
    public int size() {
        return store.size();
    }

    @Override
    public boolean isEmpty() {
        return store.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return store.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        store.clear();
        timeout.clear();
        createdAt.clear();
    }

    @Override
    public Set<K> keySet() {
        cleanup();
        return Collections.unmodifiableSet(store.keySet());
    }

    @Override
    public Collection<V> values() {
        cleanup();
        return Collections.unmodifiableCollection(store.values());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        cleanup();
        return Collections.unmodifiableSet(store.entrySet());
    }

    private void cleanup() {
        Iterator<K> iterator = new ArrayList<K>(store.keySet()).iterator();
        while (iterator.hasNext()) {
            this.get(iterator.next());
        }
    }

    public Map<K, Long> getCreatedAt() {
        return createdAt;
    }

    public Map<K, Long> getTimeout() {
        return timeout;
    }
}
