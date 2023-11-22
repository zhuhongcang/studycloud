package com.zhc.cloud.service.impl;

import com.zhc.cloud.service.RedisTestService;
import com.zhc.cloud.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhuhongcang
 * @date 2023/11/22
 */
@Slf4j
@Service
public class RedisTestServiceImpl implements RedisTestService {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String test1() {

        return null;
    }

    @Override
    public String test2(Long num) {
        return null;
    }

    @Override
    public String test3(String key) {
        redisUtil.set("test3", key);
        return redisUtil.get("test3").toString();
    }
}
