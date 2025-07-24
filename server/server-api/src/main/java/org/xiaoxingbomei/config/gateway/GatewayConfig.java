package org.xiaoxingbomei.config.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Gateway路由配置
 * 
 * @author xiaoxingbomei
 * @date 2024-01-01
 */
@Configuration
@Slf4j
public class GatewayConfig {

    /**
     * 编程式路由配置（可选，yml配置已够用）
     * 这里主要用于演示和动态路由的扩展
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 健康检查路由
                .route("health-check", r -> r
                        .path("/health/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .addResponseHeader("X-Gateway", "api-gateway"))
                        .uri("http://localhost:10001"))
                
                // 可以在这里添加更多动态路由配置
                .build();
    }
} 