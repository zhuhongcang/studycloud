package com.zhc.cloud.controller;

import com.zhc.cloud.common.response.Response;
import com.zhc.cloud.service.UserService;
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
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserService channelCodePageService;





    @PostMapping("/getById")
    public Response getById(@RequestParam("id") Long id) {
        return Response.success(channelCodePageService.getById(id));
    }
}
