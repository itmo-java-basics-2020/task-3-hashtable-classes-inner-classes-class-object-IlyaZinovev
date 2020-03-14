package ru.itmo.java;

import java.util.Map;

import static java.lang.Math.abs;

public class HashTable {
    private Entry[] array;
    private double loadFactor;
    private int threshold;
    private int size;

    public HashTable() {
        array = new Entry[4];
        loadFactor = 0.5;
        threshold = 2;
        size = 0;
    }

    public HashTable(int startLength) {
        array = new Entry[startLength];
        loadFactor = 0.5;
        threshold = (int) (startLength * loadFactor);
        size = 0;
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
        return abs(key.hashCode()) % array.length;
    }

    public int getIndex(Object key) {
        int result = getHash(key);
        while (array[result] != null && !key.equals(array[result].key)) {
            result++;
            result %= array.length;
            if (result == getHash(key)) {
                return result;
            }
        }
        return result;
    }

    public Object put(Object key, Object value) {
        Object prev = get(key);
        int index = getIndex(key);
        if (array[index] != null && !key.equals(array[index].key)) {
            while (!array[index].deleted) {
                index++;
                index %= array.length;
            }
        }
        if (array[index] == null || array[index].deleted) {
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
        if (array[index] == null || array[index].deleted || !key.equals(array[index].key)) {
            return null;
        }
        return array[index].value;
    }

    public Object remove(Object key) {
        int index = getIndex(key);
        if (array[index] == null || array[index].deleted || !key.equals(array[index].key)) {
            return null;
        }
        size--;
        array[index].deleted = true;
        return array[index].value;
    }

    public void increaseLength() {
        int len = array.length;
        threshold = (int) (len * 2 * loadFactor);
        Entry[] tmpArray = new Entry[len];
        for (int i = 0; i < len; i++) {
            if (array[i] != null && !array[i].deleted) {
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

    public static class Entry {
        private Object key;
        private Object value;
        private boolean deleted;


        public Entry (Object initKey, Object initValue) {
            key = initKey;
            value = initValue;
            deleted = false;
        }
    }
}
