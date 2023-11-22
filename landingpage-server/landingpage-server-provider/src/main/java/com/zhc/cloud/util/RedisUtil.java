package com.zhc.cloud.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author zhuhongcang
 * @date 2023/11/22
 */
@Component
public class RedisUtil {
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 删除缓存
     * @param key 一个或多个值
     * @return 成功删除个数
     */
    public long del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                if (redisTemplate.delete(key[0])) {
                    return 1;
                }
            } else {
                return redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
        return 0;
    }


    /**
     * 删除缓存
     * @param key key集合
     * @return 成功删除个数
     */
    public long del(Collection<String> key) {
        return key != null && key.size() > 0 ? redisTemplate.delete(key) : 0;
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return 是否成功
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // region String
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean setnx(String key, Object value, long time) {
        Boolean result = false;
        try {
            result = redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }
    // endregion
}
