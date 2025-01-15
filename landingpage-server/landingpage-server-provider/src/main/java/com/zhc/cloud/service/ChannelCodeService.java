package com.zhc.cloud.service;

import com.zhc.cloud.entity.ChannelCode;

/**
 * @author zhuhongcang
 * @date 2023/11/21
 */
public interface ChannelCodeService {
    ChannelCode getById(Long id);

    ChannelCode testGetById(Long id);

    int save(ChannelCode channelCode);

    int synchroChannelCode(Integer type);

    int synchroChannelCodeByJdbc(Integer type);

    void changeTableName(boolean needDel);
}
