package com.zhc.cloud.controller;

import com.zhc.cloud.common.response.Response;
import com.zhc.cloud.entity.User;
import com.zhc.cloud.service.RedisTestService;
import com.zhc.cloud.service.UserService;
import com.zhc.cloud.util.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

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
    @Resource
    private UserService userService;
    @Resource
    private RedissonClient redissonClient;

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
    public Response testGetById(@RequestParam("level") Integer level) {
        User user = new User();
        user.setId(1L);
        user.setLevel(level);
        return Response.success(userService.save(user));
    }

    @PostMapping("/redisssonTest3")
    public Response testGetByLevel(@RequestParam("level") Integer level) {
        /*int count = 0;
        String lockKey = "zhc-lock-key";
        try {
            RedisLockUtil.lock(lockKey, 2, TimeUnit.SECONDS);
            User user = new User();
            user.setId(1L);
            user.setLevel(level);
            count = userService.save(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            RedisLockUtil.unlock(lockKey);
        }
        return Response.success(count);*/
        int count = 0;
        String lockKey = "zhc-lock-key";
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.lock(2, TimeUnit.SECONDS);
            User user = new User();
            user.setId(1L);
            user.setLevel(level);
            count = userService.save(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            lock.unlock();
        }
        return Response.success(count);
    }

    @PostMapping("/redisssonTest4")
    public Response testGetById(@RequestBody User user) {
        return Response.success(userService.save(user));
    }
}
