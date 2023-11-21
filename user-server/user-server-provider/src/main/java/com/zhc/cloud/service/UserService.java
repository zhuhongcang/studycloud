package com.zhc.cloud.service;

import com.zhc.cloud.entity.User;

/**
 * @author zhuhongcang
 * @date 2023/11/21
 */
public interface UserService {
    User getById(Long id);
}
