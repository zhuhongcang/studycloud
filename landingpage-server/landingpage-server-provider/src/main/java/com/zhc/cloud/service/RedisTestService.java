package com.zhc.cloud.service;

/**
 * @author zhuhongcang
 * @date 2023/11/22
 */
public interface RedisTestService {
    String test1();

    String test2(Long num);

    String test3(String key);
}
