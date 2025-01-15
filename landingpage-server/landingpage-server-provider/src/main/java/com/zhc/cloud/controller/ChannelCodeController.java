package com.zhc.cloud.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhc.cloud.common.response.Response;
import com.zhc.cloud.controller.mapper.ChannelCodeMapper;
import com.zhc.cloud.entity.ChannelCode;
import com.zhc.cloud.request.ChannelCodeReq;
import com.zhc.cloud.service.ChannelCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * @author zhuhongcang
 * @date 2023/11/23
 */
@Slf4j
@RestController
@RequestMapping("/channelCode")
@Validated
public class ChannelCodeController {
    @Resource
    private ChannelCodeService channelCodeService;
    @Resource
    private ChannelCodeMapper channelCodeMapper;

    @PostMapping("/getById")
    public Response getById(@RequestParam("id") @Min(value = 1L, message = "id必须大于 0") Long id) {
        return Response.success(channelCodeService.getById(id));
    }
    @PostMapping("/changeCopyTable")
    public Response changeCopyTable(@RequestParam("needDel")Integer needDel) {
        channelCodeService.changeTableName(needDel==1);
        return Response.success("1");
    }

    @PostMapping("/save")
    public Response saveChannelCode(@Valid @RequestBody ChannelCodeReq channelCodeReq) {
        log.info("ChannelCodeController::saveChannelCode::param:{}", JSONObject.toJSONString(channelCodeReq));
        ChannelCode channelCode = channelCodeMapper.to(channelCodeReq);
        return Response.success(channelCodeService.save(channelCode));
    }
}
