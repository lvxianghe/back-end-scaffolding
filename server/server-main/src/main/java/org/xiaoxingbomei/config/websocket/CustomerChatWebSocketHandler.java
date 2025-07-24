package org.xiaoxingbomei.config.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 客户对话WebSocket处理器
 * 处理客户端WebSocket连接和消息
 */
@Slf4j
@Component
public class CustomerChatWebSocketHandler extends TextWebSocketHandler {

    @Resource
    private ObjectMapper objectMapper;


    /**
     * 活跃的WebSocket连接管理
     * Key: sessionId, Value: WebSocketSession
     */
    private final ConcurrentMap<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session)
    {
        String sessionId = session.getId();
        activeSessions.put(sessionId, session);
        
        log.info("WebSocket连接建立 - 会话ID: {}", sessionId);
        
        // 发送连接成功消息
        sendWelcomeMessage(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sessionId = session.getId();
        String payload = message.getPayload();
        
        // 添加调试日志：原始JSON数据
        log.info("接收到原始JSON: {}", payload);
        try{

            
            // 根据codeType进行路由处理
            handleMessageByCodeType(sessionId, payload);
            
        } catch (Exception e) {
            log.error("处理消息失败: {}", e.getMessage());
            sendErrorResponse(session, "消息处理失败，请重试");
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception
    {
        String sessionId = session.getId();
        log.error("WebSocket错误 - 会话ID: {}", sessionId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception
    {
        String sessionId = session.getId();
        activeSessions.remove(sessionId);
        
        log.info("WebSocket连接关闭 - 会话ID: {}", sessionId);
    }

    /**
     * 发送欢迎消息
     */
    private void sendWelcomeMessage(WebSocketSession session)
    {
        try
        {
            // 发送连接成功的系统消息
            var response = new java.util.HashMap<String, Object>();
            response.put("code", "0");
            response.put("message", "连接成功");
            response.put("data", new java.util.HashMap<String, String>() {{
                put("sessionId", session.getId());
            }});
            response.put("timestamp", System.currentTimeMillis());
            
            String jsonResponse = objectMapper.writeValueAsString(response);
            session.sendMessage(new TextMessage(jsonResponse));
            
            log.info("已发送欢迎消息到会话: {}", session.getId());
        } catch (Exception e) {
            log.error("发送欢迎消息失败: {}", e.getMessage());
        }
    }

    /**
     * 发送错误响应
     */
    private void sendErrorResponse(WebSocketSession session, String errorMessage) {
        try
        {
            var response = new java.util.HashMap<String, Object>();
            response.put("code", "500");
            response.put("message", errorMessage);
            response.put("timestamp", System.currentTimeMillis());
            
            String jsonResponse = objectMapper.writeValueAsString(response);
            session.sendMessage(new TextMessage(jsonResponse));
            
        } catch (Exception e) {
            log.error("发送错误响应失败: {}", e.getMessage());
        }
    }

    /**
     * 根据codeType处理消息路由
     */
    private void handleMessageByCodeType(String sessionId, String chatMessage) {

        
        log.info("处理消息路由 - 会话ID: {} - chatMessage: {}", sessionId, chatMessage);

    }
    
    /**
     * 处理聊天接收消息
     */
    private void handleChatReceiveMessage(String sessionId, String chatMessage)
    {
        log.info("处理聊天接收消息 - 会话ID: {} - chatMessage: {}", sessionId, chatMessage);
    }
    



    /**
     * 获取活跃连接数
     */
    public int getActiveConnectionCount() {
        return activeSessions.size();
    }
} 