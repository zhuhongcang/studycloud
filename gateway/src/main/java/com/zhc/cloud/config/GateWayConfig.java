package com.zhc.cloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置路由，方式可以用yml，也可以用下面这种javaconfig的方式
 * @author zhuhongcang
 * @date 2023/11/21
 */
@Configuration
public class GateWayConfig {
    @Bean
    public RouteLocator routes(RouteLocatorBuilder routeLocatorBuilder) {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("path_route_atguigu", r -> r.path("/guoji").uri("http://news.baidu.com/guonei"))
                .build();
        return routes.build();
    }
}
