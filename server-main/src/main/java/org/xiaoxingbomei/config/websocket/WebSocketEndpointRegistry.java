package org.xiaoxingbomei.config.websocket;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * WebSocket端点注册器
 * 负责注册所有WebSocket端点和相关配置
 */
@Slf4j
@Component
public class WebSocketEndpointRegistry
{

    @Resource
    private CustomerChatWebSocketHandler customerChatHandler;

    /**
     * 注册所有WebSocket端点
     * 
     * @param registry WebSocket处理器注册表
     */
    public void registerEndpoints(WebSocketHandlerRegistry registry)
    {
        // 1. 客户对话WebSocket接口
        registerCustomerChatEndpoint(registry);
        
        // 2. 未来可以在这里添加更多端点
        // registerAdminChatEndpoint(registry);
        // registerNotificationEndpoint(registry);
        
        log.info("WebSocket端点注册完成");
    }

    /**
     * 注册客户对话WebSocket端点
     */
    private void registerCustomerChatEndpoint(WebSocketHandlerRegistry registry)
    {
        String path = "/api/ws/chat";
        
        log.info("注册WebSocket端点: ws://localhost:10001{}", path);
        
        registry.addHandler(customerChatHandler, path)
                .addInterceptors(new CustomerChatHandshakeInterceptor())
                .setAllowedOrigins("*"); // 生产环境建议配置具体域名
    }
    
    /**
     * 客户对话握手拦截器
     * 处理认证、日志记录等
     */
    private static class CustomerChatHandshakeInterceptor implements HandshakeInterceptor
    {
        
        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                     WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception
        {
            
            // 这里可以添加认证逻辑
            // String token = request.getHeaders().getFirst("Authorization");
            // 暂时允许所有连接
            
            return true;
        }
        
        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Exception exception)
        {
            
            if (exception != null) {
                log.error("WebSocket握手失败: {}", exception.getMessage());
            }
        }
    }
} 