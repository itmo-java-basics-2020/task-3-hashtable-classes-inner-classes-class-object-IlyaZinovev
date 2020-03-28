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
        for (int i = 0; i < startLength; i++) {
            array[i] = Entry.NULL_ELEMENT;
        }
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

    public int getIndex(Object key) {
        int result = getHash(key);
        while (array[result].Exists() && !key.equals(array[result].key)) {
            result++;
            result %= array.length;
        }
        return result;
    }

    public Object put(Object key, Object value) {
        Object prev = get(key);
        int index = getIndex(key);
        if (!array[index].Exists()) {
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
        int index = getIndex(key);
        return array[index].value;
    }

    public Object remove(Object key) {
        int index = getIndex(key);
        if (!array[index].Exists()) {
            return null;
        }
        size--;
        Object result = array[index].value;
        array[index] = Entry.NULL_ELEMENT;
        return result;
    }

    private void increaseLength() {
        int len = array.length;
        threshold = (int) (len * 2 * loadFactor);
        Entry[] tmpArray = new Entry[len];
        for (int i = 0; i < len; i++) {
            if (array[i].Exists()) {
                tmpArray[i] = new Entry(array[i].key, array[i].value);
            }
        }
        array = new Entry[len * 2];
        for (int i = 0; i < len * 2; i++) {
            array[i] = Entry.NULL_ELEMENT;
        }
        int backupSize = size;
        for (int i = 0; i < len; i++) {
            if (tmpArray[i] != null) {
                put(tmpArray[i].key, tmpArray[i].value);
            }
        }
        size = backupSize;
    }

    public static class Entry {
        private Object key;
        private Object value;
        private static final Entry NULL_ELEMENT = new Entry(null, null);

        public Entry(Object initKey, Object initValue) {
            key = initKey;
            value = initValue;
        }

        public boolean Exists() {
            return this != NULL_ELEMENT;
        }
        }
}
