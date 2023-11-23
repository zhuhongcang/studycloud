package com.zhc.cloud.service;

import com.zhc.cloud.entity.User;

/**
 * @author zhuhongcang
 * @date 2023/11/22
 */
public interface UserService {
    User get(Long id);

    int save(User user);
}
