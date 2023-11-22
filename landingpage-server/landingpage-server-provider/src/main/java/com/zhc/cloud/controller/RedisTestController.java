package com.zhc.cloud.controller;

import com.zhc.cloud.common.response.Response;
import com.zhc.cloud.service.RedisTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhuhongcang
 * @date 2023/11/21
 */
@RestController
@RequestMapping("/redis")
@Slf4j
public class RedisTestController {
    @Resource
    private RedisTestService redisTestService;

    @PostMapping("/redisTest1")
    public Response getById() {
        return Response.success(redisTestService.test1());
    }

    @PostMapping("/redisTest2")
    public Response testGetById(@RequestParam("id") Long id) {
        return Response.success(redisTestService.test2(id));
    }

    @PostMapping("/redisTest3")
    public Response testGetById(@RequestParam("key") String key) {
        return Response.success(redisTestService.test3(key));
    }
}
