package ru.itmo.java;

import java.util.Map;

public class HashTable {
    private static final int DEFAULT_LENGTH = 4;
    private static final double DEFAULT_LOAD_FACTOR = 0.5;

    private Entry[] array;
    private double loadFactor;
    private int threshold;
    private int size;

    public HashTable() {
        this(DEFAULT_LENGTH, DEFAULT_LOAD_FACTOR);
    }

    public HashTable(int startLength) {
        this(startLength, DEFAULT_LOAD_FACTOR);
    }

    public HashTable(int startLength, double myLoadFactor) {
        array = new Entry[startLength];
        loadFactor = myLoadFactor;
        threshold = (int) (startLength * loadFactor);
        size = 0;
    }

    public int size() {
        return size;
    }

    public int getHash(Object key) {
        return java.lang.Math.abs(key.hashCode()) % array.length;
    }

    public int getIndex(Object key, boolean searchDeleted) {
        int startIndex = getHash(key);
        int result = startIndex;
        while (array[result] != null && !key.equals(array[result].key)) {
            result++;
            result %= array.length;
            if (result == startIndex) {
                break;
            }
        }
        if (array[result] == null || key.equals(array[result].key)) {
            return result;
        }
        if (searchDeleted) {
            while (!array[result].deleted) {
                result++;
                result %= array.length;
                if (result == startIndex) {
                    break;
                }
            }
        }
        return result;
    }

    public Object put(Object key, Object value) {
        Object prev = get(key);
        int index = getIndex(key, true);
        if (exists(index) && !key.equals(array[index].key)) {
            increaseLength();
            index = getIndex(key, true);
        }
        if (!exists(index)) {
            array[index] = new Entry(key, value);
            size++;
            if (size >= threshold) {
                increaseLength();
            }
            return null;
        }
        array[index] = new Entry(key, value);
        return prev;
    }

    public Object get(Object key) {
        int index = getIndex(key, false);
        if (!exists(index) || !key.equals(array[index].key)) {
            return null;
        }
        return array[index].value;
    }

    public Object remove(Object key) {
        int index = getIndex(key, false);
        if (!exists(index) || !key.equals(array[index].key)) {
            return null;
        }
        size--;
        Object result = array[index].value;
        array[index].value = null;
        array[index].deleted = true;
        return result;
    }

    private void increaseLength() {
        int len = array.length;
        threshold = (int) (len * 2 * loadFactor);
        Entry[] tmpArray = new Entry[len];
        for (int i = 0; i < len; i++) {
            if (exists(i)) {
                tmpArray[i] = new Entry(array[i].key, array[i].value);
            }
        }
        array = new Entry[len * 2];
        int backupSize = size;
        for (int i = 0; i < len; i++) {
            if (tmpArray[i] != null) {
                put(tmpArray[i].key, tmpArray[i].value);
            }
        }
        size = backupSize;
    }

    private boolean exists(int index) {
        return array[index] != null && !array[index].deleted;
    }

    public static class Entry {
        private Object key;
        private Object value;
        private boolean deleted;

        public Entry(Object initKey, Object initValue) {
            key = initKey;
            value = initValue;
            deleted = false;
        }
    }
}
