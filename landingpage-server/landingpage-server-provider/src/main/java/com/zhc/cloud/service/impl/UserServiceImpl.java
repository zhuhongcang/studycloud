package com.zhc.cloud.service.impl;

import com.zhc.cloud.dao.UserDao;
import com.zhc.cloud.entity.User;
import com.zhc.cloud.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhuhongcang
 * @date 2023/11/22
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public int save(User user) {
        int count = userDao.updateById(user);
        return count;
    }
}
