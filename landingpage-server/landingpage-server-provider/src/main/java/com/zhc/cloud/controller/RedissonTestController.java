package com.zhc.cloud.controller;

import com.zhc.cloud.common.response.Response;
import com.zhc.cloud.service.RedisTestService;
import com.zhc.cloud.util.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author zhuhongcang
 * @date 2023/11/21
 */
@RestController
@RequestMapping("/redisson")
@Slf4j
public class RedissonTestController {
    @Resource
    private RedisTestService redisTestService;

    @PostMapping("/redissonTest1")
    public Response getById() {
        String lockKey = "zhc-lock-key";
        try {
            RedisLockUtil.lock(lockKey, 2, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            RedisLockUtil.unlock(lockKey);
        }
        return Response.success(null);
    }

    @PostMapping("/redisssonTest2")
    public Response testGetById(@RequestParam("id") Long id) {
        return Response.success(redisTestService.test2(id));
    }

    @PostMapping("/redisssonTest3")
    public Response testGetById(@RequestParam("key") String key) {
        return Response.success(redisTestService.test3(key));
    }
}
