package org.xiaoxingbomei.config.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RequestPredicates;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Gateway监控配置
 * 提供响应式的健康检查和路由信息接口
 * 
 * @author xiaoxingbomei
 * @date 2024-01-01
 */
@Configuration
@Slf4j
public class GatewayMonitorConfig {

    /**
     * 创建响应式路由处理器
     * 替代传统的Controller，适配Gateway的WebFlux环境
     */
    @Bean
    public RouterFunction<ServerResponse> monitorRoutes(RouteLocator routeLocator) {
        return RouterFunctions
                // Gateway健康检查
                .route(RequestPredicates.GET("/gateway/health")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                    request -> {
                        Map<String, Object> health = new HashMap<>();
                        health.put("status", "UP");
                        health.put("service", "api-gateway");
                        health.put("timestamp", LocalDateTime.now());
                        health.put("version", "1.0.0");
                        
                        log.debug("Gateway健康检查请求");
                        
                        return ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(health));
                    })
                
                // Gateway路由信息
                .andRoute(RequestPredicates.GET("/gateway/routes")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                    request -> {
                        return routeLocator.getRoutes()
                                .collectList()
                                .flatMap(routes -> {
                                    Map<String, Object> routeInfo = new HashMap<>();
                                    routeInfo.put("totalRoutes", routes.size());
                                    routeInfo.put("timestamp", LocalDateTime.now());
                                    routeInfo.put("routes", routes.stream()
                                            .map(route -> {
                                                Map<String, Object> info = new HashMap<>();
                                                info.put("id", route.getId());
                                                info.put("uri", route.getUri().toString());
                                                info.put("order", route.getOrder());
                                                return info;
                                            })
                                            .toList());
                                    
                                    log.debug("Gateway路由信息查询，共{}条路由", routes.size());
                                    
                                    return ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .body(BodyInserters.fromValue(routeInfo));
                                });
                    })
                
                // Gateway统计信息
                .andRoute(RequestPredicates.GET("/gateway/stats")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                    request -> {
                        Map<String, Object> stats = new HashMap<>();
                        stats.put("timestamp", LocalDateTime.now());
                        stats.put("uptime", "Gateway运行中");
                        stats.put("features", new String[]{
                            "路由转发", "负载均衡", "权限认证", "请求日志", "CORS支持"
                        });
                        
                        return ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(stats));
                    });
    }
} 