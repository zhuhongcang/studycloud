package com.zhc.cloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author zhuhongcang
 * @date 2023/11/20
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication7003 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication7003.class, args);
    }
}

