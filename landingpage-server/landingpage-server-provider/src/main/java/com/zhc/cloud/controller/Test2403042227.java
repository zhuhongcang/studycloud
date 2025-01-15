package com.zhc.cloud.controller;

/**
 * @author zhuhongcang
 * @date 2024/3/4
 */
public class Test2403042227 {
    public static final int MAXIMUM_CAPACITY = 1 << 30;
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(i+"======"+tableSizeFor(i));;
        }
    }
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
}
