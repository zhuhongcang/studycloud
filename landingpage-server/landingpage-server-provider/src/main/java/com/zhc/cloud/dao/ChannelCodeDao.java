package com.zhc.cloud.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhc.cloud.entity.ChannelCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhuhongcang
 * @date 2023/11/21
 */
@Mapper
public interface ChannelCodeDao extends BaseMapper<ChannelCode> {
    /**
     * 批量插入
     *
     * @param list 渠道数据List
     * @return 数据库影响行数
     */
    int batchInsert(List<ChannelCode> list);
    /**
     * 截断表
     *
     * @return 数据库影响行数
     */
    int truncateTable();
}
