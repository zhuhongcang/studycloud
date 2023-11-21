package com.zhc.cloud.controller;

import com.zhc.cloud.common.response.Response;
import com.zhc.cloud.response.LandingPageResp;
import com.zhc.cloud.service.LandingPageService;
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
@RequestMapping("/landingpage")
@Slf4j
public class LandingPageController {
    @Resource
    private LandingPageService landingPageService;

    @PostMapping
    public Response<LandingPageResp> getById(@RequestParam("id") Long id) {
        return null;
    }
}
