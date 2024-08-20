package io.github.kuggek.editor.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A bidirectional map that has a one-to-one relationship between keys and values. This means that
 * each key maps to exactly one value, and each value maps to exactly one key. 
 */
public class BiMap<T, U> implements Map<T, U> {
    private Map<T, U> map;
    private Map<U, T> inverseMap;
   
    public BiMap() {
        map = new HashMap<>();
        inverseMap = new HashMap<>();
    }

    @Override
    public U put(T key, U value) {
        U val = map.put(key, value);
        inverseMap.put(value, key);
        return val;
    }

    @Override
    public void putAll(Map<? extends T, ? extends U> m) {
        for (Entry<? extends T, ? extends U> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public U get(Object key) {
        return map.get(key);
    }

    public T getKey(U value) {
        return inverseMap.get(value);
    }

    @Override
    public U remove(Object key) {
        U value = map.get(key);
        map.remove(key);
        inverseMap.remove(value);
        return value;
    }

    public void removeValue(U value) {
        T key = inverseMap.get(value);
        map.remove(key);
        inverseMap.remove(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return inverseMap.containsKey(value);
    }

    @Override
    public void clear() {
        map.clear();
        inverseMap.clear();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    @Override
    public Set<T> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<U> values() {
        return inverseMap.keySet();
    }

    @Override
    public Set<Entry<T, U>> entrySet() {
        return map.entrySet();
    }
}
