package com.zhc.cloud.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhc.cloud.dao.ChannelCodeDao;
import com.zhc.cloud.entity.ChannelCode;
import com.zhc.cloud.service.ChannelCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author zhuhongcang
 * @date 2023/11/21
 */
@Slf4j
@Service
public class ChannelCodeServiceImpl implements ChannelCodeService {
    @Resource
    private ChannelCodeDao channelCodeDao;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public ChannelCode getById(Long id) {
        ChannelCode channelCode = channelCodeDao.selectById(id);
        return channelCode;
    }

    @Override
    public ChannelCode testGetById(Long id) {
        ChannelCode channelCode = channelCodeDao.selectById(id);
        redisTemplate.opsForValue().set("channelCode" + channelCode.getChannelId(), JSONObject.toJSONString(channelCode));
        return channelCode;
    }

    @Override
    public int save(ChannelCode channelCode) {
        int count = 0;
        if (Objects.nonNull(channelCode) && Objects.nonNull(channelCode.getChannelId())) {
            count = channelCodeDao.updateById(channelCode);
        } else {
            count = channelCodeDao.insert(channelCode);
        }
        return count;
    }
}
