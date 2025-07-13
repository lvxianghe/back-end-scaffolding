package org.xiaoxingbomei.config.websocket;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


/**
 * WebSocket路由配置管理器
 * 统一管理所有WebSocket端点的注册
 * 支持多个WebSocket接口扩展
 * 
 * @author NodeSk Team
 * @since 2024-01-01
 */
@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketRoutingConfig implements WebSocketConfigurer
{

    @Resource
    private WebSocketEndpointRegistry endpointRegistry;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        log.info("开始注册WebSocket端点");
        
        // 通过端点注册器统一注册所有WebSocket接口
        endpointRegistry.registerEndpoints(registry);
        
        log.info("所有WebSocket端点注册完成");
        log.info("WebSocket路由配置完成");
    }
} 