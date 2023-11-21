package com.zhc.cloud.service.impl;

import com.zhc.cloud.dao.UserDao;
import com.zhc.cloud.entity.User;
import com.zhc.cloud.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhuhongcang
 * @date 2023/11/21
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public User getById(Long id) {
        User user = userDao.selectById(id);
        return user;
    }
}
