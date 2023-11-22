package com.zhc.cloud.config;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhuhongcang
 * @date 2023/11/22
 */

@Data
@Configuration
public class RedissonConfig {
    private static final String REDISSON_PREFIX = "redis://";
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password:}")
    private String password;

    @Bean
    public RedissonClient getRedissonClient() {
        Config config = new Config();
        // 设置编解码器
        config.setCodec(new JsonJacksonCodec());
        //单节点
        config.useSingleServer().setAddress(REDISSON_PREFIX + host + ":" + port);
        if (StringUtils.isEmpty(password)) {
            config.useSingleServer().setPassword(null);
        } else {
            config.useSingleServer().setPassword(password);
        }
        //添加主从配置
        // config.useMasterSlaveServers().setMasterAddress("").setPassword("").addSlaveAddress(new String[]{"",""});
        // 集群模式配置 setScanInterval()扫描间隔时间，单位是毫秒, //可以用"rediss://"来启用SSL连接
        // config.useClusterServers().setScanInterval(2000).addNodeAddress("redis://127.0.0.1:7000", "redis://127.0.0.1:7001").addNodeAddress("redis://127.0.0.1:7002");
        return Redisson.create(config);
    }
}
