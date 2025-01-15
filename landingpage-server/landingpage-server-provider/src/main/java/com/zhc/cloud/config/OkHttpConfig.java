package com.zhc.cloud.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author zhuhongcang
 * @date 2024/3/30
 */
@Configuration
public class OkHttpConfig {
    public static final ConnectionPool CONNECTION_POOL = new ConnectionPool(128, 5L, TimeUnit.MINUTES);
    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient().newBuilder().readTimeout(120,TimeUnit.SECONDS).callTimeout(120,TimeUnit.SECONDS).connectionPool(CONNECTION_POOL).build();
    }
}
