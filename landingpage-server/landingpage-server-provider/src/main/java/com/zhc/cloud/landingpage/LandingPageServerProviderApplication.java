package com.zhc.cloud.landingpage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author zhuhongcang
 * @date 2023/11/21
 */
@EnableEurekaClient
@SpringBootApplication
public class LandingPageServerProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(LandingPageServerProviderApplication.class, args);
    }
}
