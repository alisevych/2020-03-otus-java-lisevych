package ru.otus.helpers;

import java.util.HashMap;
import java.util.Map;

public class HashMapHelper<K,V> {

    public Map<K, V> deepMapCopy(Map<K, V> initialMap) {
        var copy = new HashMap<K,V>();
        for (var cell : initialMap.entrySet()){
            copy.put(cell.getKey(), cell.getValue());
        }
        return copy;
    }

}
