/*
package com.zhc.cloud.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

*/
/**
 * @author zhuhongcang
 * @date 2023/11/22
 *//*

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedissonConfig {

    private String host;

    private int port;

    @Bean
    public RedissonClient getRedissonClient() {
        Config config = new Config();
        // 设置编解码器
        config.setCodec(new JsonJacksonCodec());
        // 模式设置 单例
        config.useSingleServer();
        config.useSingleServer().setAddress("redis://" + host + ":" + port);
        config.setCodec(new JsonJacksonCodec());
        return Redisson.create(config);
    }

}
*/
