package com.zhc.cloud.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhc.cloud.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhuhongcang
 * @date 2023/11/22
 */
@Mapper
public interface UserDao extends BaseMapper<User> {
}
