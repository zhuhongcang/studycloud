package com.zhc.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhc.cloud.dao.UserDao;
import com.zhc.cloud.entity.User;
import com.zhc.cloud.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAuid, "test");
        User user1=userDao.selectOne(queryWrapper);
        String account = "12";
        List<User> list = new ArrayList<>();
        list.add(new User());
        list.stream().filter(o -> account.equals(o.getAccount())).collect(Collectors.toList());

        test(list);
        String[] aaa = new String[]{"111"};
        return user;
    }

    private void test(List<User> list) {
        for(int i=0; i<list.size();i++){
            System.out.println(list.get(i).getAccount());
        }
    }
}
