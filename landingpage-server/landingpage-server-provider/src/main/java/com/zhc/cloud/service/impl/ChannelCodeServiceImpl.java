package com.zhc.cloud.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.zhc.cloud.dao.ChannelCodeCopyDao;
import com.zhc.cloud.dao.ChannelCodeDao;
import com.zhc.cloud.entity.ChannelCode;
import com.zhc.cloud.service.ChannelCodeService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    private ChannelCodeCopyDao channelCodeCopyDao;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RedissonClient redissonClient;

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

    @Override
    public int synchroChannelCode(Integer type) {
        int count = 0;
        String lockKey = "zhc-lock-key";
        RLock lock = redissonClient.getLock(lockKey);
        boolean lockAcquired = false;
        try {
            //加锁，参数：获取锁的最大等待时间（期间会重试），锁自动释放时间，时间单位
            //注意：如果指定锁自动释放时间，不管业务有没有执行完，锁都不会自动延期，即没有 watch dog 机制。
//            lockAcquired = lock.tryLock(1, 2, TimeUnit.SECONDS);
            log.info("time:{}；threadName:{}； :lockAcquired{}", System.currentTimeMillis(),Thread.currentThread().getName(), lockAcquired);
            lockAcquired = lock.tryLock(1, TimeUnit.SECONDS);
            if (lockAcquired) {
                log.info("=======================time:{}；threadName:{}进来了", System.currentTimeMillis(),Thread.currentThread().getName());
                Integer total = channelCodeDao.selectCount(new LambdaQueryWrapper<>());
                int pageSize = 20000;
                int totalPage = total % pageSize > 0 ? total / pageSize + 1 : total / pageSize;
                channelCodeCopyDao.truncateTable();
                for (int i = 1; i <= totalPage; i++) {
                    LambdaQueryWrapper<ChannelCode> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.orderByAsc(ChannelCode::getChannelId);
                    PageHelper.startPage(i, pageSize, false);
                    List<ChannelCode> channelCodeList = channelCodeDao.selectList(queryWrapper);
                    count += channelCodeCopyDao.batchInsert(channelCodeList);
                }
                if (count == total) {
                    // code->tmp
                    channelCodeCopyDao.renameChannelCodeTmp();
                    // copy->code
                    channelCodeCopyDao.renameChannelCode();
                    // tmp->copy
                    channelCodeCopyDao.renameChannelCodeCopy();
                }
                log.info("=======================time:{}；threadName:{}执行完了", System.currentTimeMillis(),Thread.currentThread().getName());
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            // 释放锁
            if (lockAcquired) {
                log.info("=======================time:{}；threadName:{}释放锁了", System.currentTimeMillis(),Thread.currentThread().getName());
                redissonClient.getLock(lockKey).unlock();
            }
        }
        return count;
    }
}
