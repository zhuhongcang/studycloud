package com.zhc.cloud.controller;

import com.zhc.cloud.common.response.Response;
import com.zhc.cloud.service.ChannelCodeService;
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
@RequestMapping("/channelCode")
@Slf4j
public class RedisTestController {
    @Resource
    private ChannelCodeService channelCodePageService;

    @PostMapping("/getById")
    public Response getById(@RequestParam("id") Long id) {
        return Response.success(channelCodePageService.getById(id));
    }
    @PostMapping("/testGetById")
    public Response testGetById(@RequestParam("id") Long id) {
        return Response.success(channelCodePageService.testGetById(id));
    }
}
