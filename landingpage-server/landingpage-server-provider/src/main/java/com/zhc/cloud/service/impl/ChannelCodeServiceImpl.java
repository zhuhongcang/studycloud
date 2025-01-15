package com.zhc.cloud.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhc.cloud.dao.ChannelCodeCopyDao;
import com.zhc.cloud.dao.ChannelCodeDao;
import com.zhc.cloud.entity.ChannelCode;
import com.zhc.cloud.service.ChannelCodeService;
import com.zhc.cloud.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author zhuhongcang
 * @date 2023/11/21
 */
@Slf4j
@Service
public class ChannelCodeServiceImpl implements ChannelCodeService {
    public static final ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(
            30, 30, 300, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1000),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "thread_pool_channle_code_handle_task_" + r.hashCode());
                }
            }, new ThreadPoolExecutor.CallerRunsPolicy()
    );
    @Resource
    private ChannelCodeDao channelCodeDao;
    @Resource
    private ChannelCodeCopyDao channelCodeCopyDao;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    private OkHttpClient okHttpClient;

    @Override
    public ChannelCode getById(Long id) {
        ChannelCode channelCode = null;
        try {
            /*Object obj = redisTemplate.opsForValue().get(Constants.CHANNEL_ID_KEY + id);
            channelCode = obj == null ? null : JSONObject.parseObject(obj.toString(), ChannelCode.class);
            if (channelCode == null) {
                channelCode = channelCodeDao.selectById(id);
                if (channelCode != null) {
                    redisTemplate.opsForValue().set(Constants.CHANNEL_ID_KEY + id, JSONObject.toJSONString(channelCode));
                }
            }*/
            channelCode = channelCodeDao.selectById(id);
            Thread.sleep(100);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
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

    /**
     * 总数据 4824546
     * 482w 单线程 mybatisplus 每次2000条耗时  3061930ms=3062s=51.03min
     * 482w 通过jdbc 批量处理  10个线程  每2000条执行一次  20min
     * 482w 线程池（最小30，最大30，队列1000） mybatisplus 每2000条开一个线程去执行 9分钟
     *
     * @param type
     * @return
     */
    @Override
    public int synchroChannelCode(Integer type) {
        return type == 0 ? synchroChannelCodeBySingleThread() : synchroChannelCodeByThreadPool();
    }

    @Override
    public int synchroChannelCodeByJdbc(Integer type) {
        int count = 0;
        String createTotalRedisKey = "zhc-lock-key-incr";
        if (redisUtil.get(createTotalRedisKey) != null) {
            //正在进行中
            return count;
        }
        THREAD_POOL.submit(() -> {
            redisUtil.set(createTotalRedisKey, 0, 1800);
            int threadSize = 10;
            // 总数
            Integer total = 4824546;
            int pageSize = 2000;
            int totalPage = total % pageSize > 0 ? total / pageSize + 1 : total / pageSize;
            channelCodeCopyDao.truncateTable();
            /*final CountDownLatch countDownLatch = new CountDownLatch(threadSize);*/
            //每个线程负责多少页
            int pageSizeForThread = totalPage / 10;
            SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
            // 分十个线程
            for (int i = 1; i <= threadSize; i++) {
                int startPageNum = (i - 1) * pageSizeForThread + 1;
                int endPgeNum = i * pageSizeForThread;
                endPgeNum = i == threadSize ? totalPage : endPgeNum;
                int finalEndPgeNum = endPgeNum;
                THREAD_POOL.submit(() -> {
                    SqlSession sqlSession = null;
                    Connection connection = null;
                    PreparedStatement ps = null;
                    try {
                        StopWatch stopWatch = new StopWatch();
                        stopWatch.start("jdbc savebatch");
                        sqlSession = sqlSessionFactory.openSession(false);
                        connection = sqlSession.getConnection();
                        String sql = "INSERT INTO channel_code_copy(`channel_id`, `channel_code`, `channel_name`, `source_type_id`, `source_type_name`, `auid`, `aname`, `utm_source`, `utm_plan`, `account`, `plan_name`, `vst`, `create_time`, `is_hide`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        /*connection.setAutoCommit(false);*/
                        ps = connection.prepareStatement(sql);
                        for (int k = startPageNum; k <= finalEndPgeNum; k++) {
                            List<ChannelCode> channelCodeList = new LinkedList<>();
                            StopWatch totalWatch = new StopWatch();
                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");
                            Request request = new Request.Builder()
                                    .url(OUTER_URL + k + "&pageSize=" + pageSize)
                                    .post(requestBody) //post请求
                                    .build();
                            final Call call = okHttpClient.newCall(request);
                            try {
                                Response response = call.execute();
                                JSONObject jsonObject = JSONObject.parseObject(response.body().string());
                                if (jsonObject != null) {
                                    channelCodeList = jsonObject.getJSONArray("data").toJavaList(ChannelCode.class);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            totalWatch.stop();
                            log.info("=======================okhttp耗时:{}", totalWatch.getLastTaskTimeMillis());
                            for (int j = 0; j < channelCodeList.size(); j++) {
                                ps = setStatement(ps, channelCodeList.get(j));
                                ps.addBatch();
                            }
                            int[] arr = ps.executeBatch();
                            ps.clearBatch();
                        }
                        stopWatch.stop();
                        log.info("=======================threadName:{}执行完了，time:{}", stopWatch.getLastTaskName(), stopWatch.getLastTaskTimeMillis());
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    } finally {
                        /*countDownLatch.countDown();*/
                        if (ps != null) {
                            try {
                                ps.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (connection != null) {
                            try {
                                connection.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (sqlSession != null) {
                            sqlSession.close();
                        }
                    }
                });
            }
        });
        return count;
    }

    private PreparedStatement setStatement(PreparedStatement ps, ChannelCode channelCode) throws SQLException {
        ps.setLong(1, channelCode.getChannelId());
        ps.setLong(2, channelCode.getChannelCode());
        if (channelCode.getChannelName() == null) {
            ps.setNull(3, Types.VARCHAR);
        } else {
            ps.setString(3, channelCode.getChannelName());
        }
        if (channelCode.getSourceTypeId() == null) {
            ps.setNull(4, Types.BIGINT);
        } else {
            ps.setLong(4, channelCode.getSourceTypeId());
        }
        if (channelCode.getSourceTypeName() == null) {
            ps.setNull(5, Types.VARCHAR);
        } else {
            ps.setString(5, channelCode.getSourceTypeName());
        }
        if (channelCode.getAuid() == null) {
            ps.setNull(6, Types.BIGINT);
        } else {
            ps.setLong(6, channelCode.getAuid());
        }
        if (channelCode.getAname() == null) {
            ps.setNull(7, Types.VARCHAR);
        } else {
            ps.setString(7, channelCode.getAname());
        }
        if (channelCode.getUtm_source() == null) {
            ps.setNull(8, Types.VARCHAR);
        } else {
            ps.setString(8, channelCode.getUtm_source());
        }
        if (channelCode.getUtm_plan() == null) {
            ps.setNull(9, Types.BIGINT);
        } else {
            ps.setLong(9, channelCode.getUtm_plan());
        }
        if (channelCode.getAccount() == null) {
            ps.setNull(10, Types.VARCHAR);
        } else {
            ps.setString(10, channelCode.getAccount());
        }
        if (channelCode.getPlanName() == null) {
            ps.setNull(11, Types.VARCHAR);
        } else {
            ps.setString(11, channelCode.getPlanName());
        }
        if (channelCode.getVst() == null) {
            ps.setNull(12, Types.BIGINT);
        } else {
            ps.setLong(12, channelCode.getVst());
        }
        ps.setDate(13, new Date(channelCode.getCreateTime().getTime()));
        if (channelCode.getIsHide() == null) {
            ps.setNull(14, Types.INTEGER);
        } else {
            ps.setInt(14, channelCode.getIsHide());
        }
        return ps;
    }

    /**
     * 外部获取渠道号信息的分页接口url
     */
    public static final String OUTER_URL = "http:\\/\\/127.0.0.1:28079/landingpage/channelCode/findPage?pageIndex=";

    /**
     * 单线程去执行
     *
     * @return
     */
    public int synchroChannelCodeBySingleThread() {
        int count = 0;
        String lockKey = "zhc-lock-key";
        RLock lock = redissonClient.getLock(lockKey);
        boolean lockAcquired = false;
        try {
            //加锁，参数：获取锁的最大等待时间（期间会重试），锁自动释放时间，时间单位
            //注意：如果指定锁自动释放时间，不管业务有没有执行完，锁都不会自动延期，即没有 watch dog 机制。
            //lockAcquired = lock.tryLock(1, 2, TimeUnit.SECONDS);
            log.info("time:{}；threadName:{}； :lockAcquired{}", System.currentTimeMillis(), Thread.currentThread().getName(), lockAcquired);
            lockAcquired = lock.tryLock(1, TimeUnit.SECONDS);
            if (lockAcquired) {
                THREAD_POOL.submit(() -> {
                    try {
                        int createCount = 0;
                        StopWatch stopWatch = new StopWatch();
                        stopWatch.start("单线程同步渠道号任务");
                        /*Integer total = channelCodeDao.selectCount(new LambdaQueryWrapper<>());*/
                        Integer total = 4824546;
                        int pageSize = 2000;
                        int totalPage = total % pageSize > 0 ? total / pageSize + 1 : total / pageSize;
                        channelCodeCopyDao.truncateTable();
                        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");
                        for (int i = 1; i <= totalPage; i++) {
                            Request request = new Request.Builder()
                                    .url(OUTER_URL + i + "&pageSize=" + pageSize)
                                    .post(requestBody) //post请求
                                    .build();
                            final Call call = okHttpClient.newCall(request);
                            List<ChannelCode> channelCodeList = new LinkedList<>();
                            Response response = call.execute();
                            JSONObject jsonObject = JSONObject.parseObject(response.body().string());
                            if (jsonObject != null) {
                                channelCodeList = jsonObject.getJSONArray("data").toJavaList(ChannelCode.class);
                            }
                            createCount += channelCodeCopyDao.batchInsert(channelCodeList);
                        }
                        if (createCount == total) {
                            changeTableName(true);
                        }
                        stopWatch.stop();
                        log.info("======单线程================={}执行完毕，总耗时:{}ms；", stopWatch.getLastTaskName(), stopWatch.getTotalTimeMillis());
                    } catch (Exception e) {
                        log.error("", e);
                    }
                });
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            // 释放锁
            if (lockAcquired) {
                log.info("=======================time:{}；threadName:{}释放锁了", System.currentTimeMillis(), Thread.currentThread().getName());
                redissonClient.getLock(lockKey).unlock();
            }
        }
        return count;
    }

    /**
     * 线程池版本
     *
     * @return
     */
    public int synchroChannelCodeByThreadPool() {
        int count = 0;
        String createTotalRedisKey = "zhc-lock-key-incr" + System.currentTimeMillis();
        try {
            if (redisUtil.get(createTotalRedisKey) == null) {
                redisUtil.set(createTotalRedisKey, 0);
                log.info("=======================time:{}；threadName:{}进来了", System.currentTimeMillis(), Thread.currentThread().getName());
                /*Integer total = channelCodeDao.selectCount(new LambdaQueryWrapper<>());*/
                Integer total = 4824546;
                int pageSize = 2000;
                int totalPage = total % pageSize > 0 ? total / pageSize + 1 : total / pageSize;
                channelCodeCopyDao.truncateTable();
                THREAD_POOL.submit(() -> {
                    StopWatch totalWatch = new StopWatch();
                    final CountDownLatch countDownLatch = new CountDownLatch(totalPage);
                    for (int i = 1; i <= totalPage; i++) {
                        int finalI = i;
                        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");
                        THREAD_POOL.submit(() -> {
                            try {
                                StopWatch stopWatch = new StopWatch();
                                stopWatch.start();
                                Request request = new Request.Builder()
                                        .url(OUTER_URL + finalI + "&pageSize=" + pageSize)
                                        .post(requestBody) //post请求
                                        .build();
                                final Call call = okHttpClient.newCall(request);
                                List<ChannelCode> channelCodeList = new LinkedList<>();
                                Response response = call.execute();
                                JSONObject jsonObject = JSONObject.parseObject(response.body().string());
                                if (jsonObject != null) {
                                    channelCodeList = jsonObject.getJSONArray("data").toJavaList(ChannelCode.class);
                                }
                                int createCount = channelCodeCopyDao.batchInsert(channelCodeList);
                                // 往redis中累加成功同步的数据
                                redisUtil.incr(createTotalRedisKey, createCount);
                                stopWatch.stop();
                                log.info("=======================threadName:{}执行完了，time:{}", stopWatch.getLastTaskName(), stopWatch.getLastTaskTimeMillis());
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                            } finally {
                                countDownLatch.countDown();
                            }
                        });
                    }
                    try {
                        countDownLatch.await(30, TimeUnit.MINUTES);
                        if (total.equals(redisUtil.get(createTotalRedisKey))) {
                            changeTableName(true);
                            // 同步成功，删除同步相关的redisKey
                            redisUtil.del(createTotalRedisKey);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    totalWatch.stop();
                    log.info("=======================总执行:{}总执行时间:{}", totalWatch.getLastTaskName(), totalWatch.getLastTaskTimeMillis());
                });
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return count;
    }

    public int synchroChannelCodeByThreadPool11() {
        int count = 0;
        String createTotalRedisKey = "zhc-lock-key-incr" + System.currentTimeMillis();
        try {
            if (redisUtil.get(createTotalRedisKey) == null) {
                redisUtil.set(createTotalRedisKey, 0);
                log.info("=======================time:{}；threadName:{}进来了", System.currentTimeMillis(), Thread.currentThread().getName());
                /*Integer total = channelCodeDao.selectCount(new LambdaQueryWrapper<>());*/
                Integer total = 4824546;
                int pageSize = 2000;
                int totalPage = total % pageSize > 0 ? total / pageSize + 1 : total / pageSize;
                channelCodeCopyDao.truncateTable();
                THREAD_POOL.submit(() -> {
                    StopWatch totalWatch = new StopWatch();
                    final CountDownLatch countDownLatch = new CountDownLatch(totalPage);
                    final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");
                    for (int i = 1; i <= totalPage; i++) {
                        Request request = new Request.Builder()
                                .url(OUTER_URL + i + "&pageSize=" + pageSize)
                                .post(requestBody) //post请求
                                .build();
                        final Call call = okHttpClient.newCall(request);
                        List<ChannelCode> channelCodeList = new LinkedList<>();
                        try {
                            Response response = call.execute();
                            JSONObject jsonObject = JSONObject.parseObject(response.body().string());
                            if (jsonObject != null) {
                                channelCodeList = jsonObject.getJSONArray("data").toJavaList(ChannelCode.class);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        List<ChannelCode> finalChannelCodeList = channelCodeList;
                        THREAD_POOL.submit(() -> {
                            try {
                                StopWatch stopWatch = new StopWatch();
                                stopWatch.start();
                                int createCount = channelCodeCopyDao.batchInsert(finalChannelCodeList);
                                // 往redis中累加成功同步的数据
                                redisUtil.incr(createTotalRedisKey, createCount);
                                stopWatch.stop();
                                log.info("=======================threadName:{}执行完了，time:{}", stopWatch.getLastTaskName(), stopWatch.getLastTaskTimeMillis());
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                            } finally {
                                countDownLatch.countDown();
                            }
                        });
                    }
                    try {
                        countDownLatch.await(30, TimeUnit.MINUTES);
                        if (total.equals(redisUtil.get(createTotalRedisKey))) {
                            changeTableName(true);
                            // 同步成功，删除同步相关的redisKey
                            redisUtil.del(createTotalRedisKey);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    totalWatch.stop();
                    log.info("=======================总执行:{}总执行时间:{}", totalWatch.getLastTaskName(), totalWatch.getLastTaskTimeMillis());
                });
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return count;
    }

    /**
     * 交换copy表和channelCode表名
     */
    @Override
    public void changeTableName(boolean needDel) {
        // code->tmp
        channelCodeCopyDao.renameChannelCodeTmp();
        // copy->code
        channelCodeCopyDao.renameChannelCode();
        // tmp->copy
        channelCodeCopyDao.renameChannelCodeCopy();
        if (needDel) {
            channelCodeCopyDao.truncateTable();
        }
    }
}
