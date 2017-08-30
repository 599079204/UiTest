package com.ljw.uitest.utils;

import android.support.v4.util.ArrayMap;

import java.util.Map;

/**
 * 特殊的Map. 键,值都是自定义的类型
 * Created by ljw on 2017/8/30.
 */

public class KeyValuePair<Key, Value> {

    public final Key key;
    public final Value value;

    public KeyValuePair(Key key, Value value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 转换
     */
    public static <Key, Value> Map<Key, Value> convert2Map(KeyValuePair<Key, Value>... keyValuePairs) {
        ArrayMap<Key, Value> map = new ArrayMap<>();
        for (KeyValuePair<Key, Value> pair : keyValuePairs) {
            map.put(pair.key, pair.value);
        }
        return map;
    }
}
