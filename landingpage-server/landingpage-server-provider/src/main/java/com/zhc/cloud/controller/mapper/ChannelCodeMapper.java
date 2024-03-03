package com.zhc.cloud.controller.mapper;

import com.zhc.cloud.common.mapper.IMapper;
import com.zhc.cloud.entity.ChannelCode;
import com.zhc.cloud.request.ChannelCodeReq;
import org.springframework.stereotype.Component;

/**
 * @author zhuhongcang
 * @date 2023/11/23
 */
@Component
public class ChannelCodeMapper implements IMapper<ChannelCodeReq, ChannelCode> {
    @Override
    public ChannelCode to(ChannelCodeReq source) {
        ChannelCode channelCode = new ChannelCode();
        channelCode.setChannelId(source.getChannelId());
        channelCode.setChannelCode(source.getChannelCode());
        channelCode.setChannelName(source.getChannelName());
        channelCode.setSourceTypeId(source.getSourceTypeId());
        channelCode.setSourceTypeName(source.getSourceTypeName());
        channelCode.setUtm_source(source.getUtmSource());
        channelCode.setUtm_plan(source.getUtmPlan());
        channelCode.setAccount(source.getAccount());
        channelCode.setPlanName(source.getPlanName());
        channelCode.setVst(source.getVst());
        return channelCode;
    }

    @Override
    public ChannelCodeReq from(ChannelCode target) {
        return null;
    }
}
