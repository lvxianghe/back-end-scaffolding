package org.xiaoxingbomei.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Gateway全局日志过滤器
 * 记录请求和响应的基本信息
 */
@Component
@Slf4j
public class GlobalLogFilter implements GlobalFilter, Ordered
{

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        
        // 记录请求开始时间
        long startTime = System.currentTimeMillis();
        
        // 记录请求信息
        String requestId = request.getId();
        String method = request.getMethod().name();
        URI uri = request.getURI();
        String clientIp = getClientIp(request);
        
        log.info("🚀 Gateway请求开始 - ID: {}, Method: {}, URI: {}, ClientIP: {}", 
                requestId, method, uri, clientIp);
        
        return chain.filter(exchange).then(
            Mono.fromRunnable(() -> {
                // 记录响应信息
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                int statusCode = response.getStatusCode() != null ? 
                    response.getStatusCode().value() : 0;
                
                log.info("✅ Gateway请求完成 - ID: {}, Status: {}, Duration: {}ms", 
                        requestId, statusCode, duration);
            })
        );
    }

    /**
     * 获取客户端真实IP
     */
    private String getClientIp(ServerHttpRequest request)
    {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddress() != null ? 
            request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
    }

    @Override
    public int getOrder()
    {
        // 最高优先级，最先执行
        return Ordered.HIGHEST_PRECEDENCE;
    }
} 