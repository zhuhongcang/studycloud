package com.zhc.cloud.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhuhongcang
 * @date 2024/3/4
 */
public class Test240304 {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
//        Integer count = map.get("count");
        for (int i = 0; i < 10000; i++) {
            map.put("key" + i, i);
        }
        System.out.println(map);
        HashMap<String, Integer> hashmap = new HashMap<>();
        hashmap.put("count", 123213123);
    }
}
