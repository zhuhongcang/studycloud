package com.zhc.cloud.landingpage.service.impl;

import com.zhc.cloud.landingpage.dao.ChannelCodeDao;
import com.zhc.cloud.landingpage.entity.ChannelCode;
import com.zhc.cloud.landingpage.service.ChannelCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhuhongcang
 * @date 2023/11/21
 */
@Service
@Slf4j
public class ChannelCodeServiceImpl implements ChannelCodeService {
    @Resource
    private ChannelCodeDao channelCodeDao;

    @Override
    public ChannelCode getById(Long id) {
        ChannelCode channelCode = channelCodeDao.selectById(id);
        return channelCode;
    }
}
