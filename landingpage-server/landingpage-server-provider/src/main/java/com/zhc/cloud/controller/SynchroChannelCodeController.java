package com.zhc.cloud.controller;

import com.zhc.cloud.common.response.Response;
import com.zhc.cloud.service.ChannelCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Min;

/**
 * @author zhuhongcang
 * @date 2024/3/2
 */
@Slf4j
@RestController
@RequestMapping("/syncChannelCode")
@Validated
public class SynchroChannelCodeController {

    @Resource
    private ChannelCodeService channelCodeService;

    @PostMapping("/syncChannelCode")
    public Response syncChannelCode(@RequestParam("type") @Min(value = 0L, message = "type必须大于 0") Integer type) {
        return Response.success(channelCodeService.synchroChannelCode(type));
    }
}
