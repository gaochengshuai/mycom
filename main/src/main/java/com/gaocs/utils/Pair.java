package com.gaocs.utils;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.utils
 * @Description:
 * @time:2021/1/18 15:12
 */
public class Pair<T, T1> {

    public Pair(T version, T1 v) {

    }
    private static String key;
    private static Double value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        Pair.key = key;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        Pair.value = value;
    }
}
