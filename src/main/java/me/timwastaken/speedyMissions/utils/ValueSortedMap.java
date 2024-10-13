package me.timwastaken.speedyMissions.utils;

import java.util.*;

public class ValueSortedMap<K, V extends Comparable<V>> extends AbstractMap<K, V> {
    private final Map<K, V> map;
    private final TreeMap<K, V> sortedMap;

    public ValueSortedMap(Comparator<V> valueComparator, Comparator<K> keyComparator) {
        this.map = new HashMap<>(); // This is the map that stores key-value pairs

        // Create a custom comparator for the sorted map
        Comparator<K> valueComparatorByKey = (key1, key2) -> {
            V value1 = map.get(key1);
            V value2 = map.get(key2);
            // Compare values first
            int comparison = valueComparator.compare(value1, value2);
            if (keyComparator == null || comparison != 0) {
                return comparison;
            }
            // If values are equal, use key comparison as a tie-breaker
            return keyComparator.compare(key1, key2);
        };

        this.sortedMap = new TreeMap<>(valueComparatorByKey); // This keeps keys sorted by their values
    }

    @Override
    public V put(K key, V value) {
        // Remove the key from the sorted map if it already exists
        if (map.containsKey(key)) {
            sortedMap.remove(key);
        }
        // Put it in both maps
        map.put(key, value);
        sortedMap.put(key, value);
        return value;
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V remove(Object key) {
        V removedValue = map.remove(key);
        sortedMap.remove(key);
        return removedValue;
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return sortedMap.entrySet();
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
    public void clear() {
        map.clear();
        sortedMap.clear();
    }
}

