package org.xiaoxingbomei.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.xiaoxingbomei.service.auth.SsoService;
import org.xiaoxingbomei.service.auth.UserService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * SSO单点登录服务实现
 */
@Service
@Slf4j
public class SsoServiceImpl implements SsoService
{

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserService userService;

    private static final String SSO_TICKET_KEY = "auth:sso:ticket:";
    private static final String SSO_TOKEN_KEY = "auth:sso:token:";
    private static final String SERVICE_TOKEN_KEY = "auth:service:token:";
    private static final int TICKET_EXPIRE_MINUTES = 5; // 票据5分钟有效
    private static final int SSO_TOKEN_EXPIRE_HOURS = 8; // SSO Token 8小时有效
    private static final int SERVICE_TOKEN_EXPIRE_MINUTES = 30; // 服务间Token 30分钟有效

    @Override
    public String ssoLogin(Long userId, String service)
    {
        log.info("SSO登录请求: userId={}, service={}", userId, service);

        try {
            // 生成SSO票据
            String ticket = "ST-" + UUID.randomUUID().toString().replace("-", "");

            // 存储票据信息
            Map<String, Object> ticketInfo = new HashMap<>();
            ticketInfo.put("userId", userId);
            ticketInfo.put("service", service);
            ticketInfo.put("createTime", LocalDateTime.now().toString());

            // 票据存储到Redis，5分钟有效
            stringRedisTemplate.opsForValue().set(
                    SSO_TICKET_KEY + ticket,
                    ticketInfo.toString(),
                    TICKET_EXPIRE_MINUTES,
                    TimeUnit.MINUTES
            );

            log.info("SSO票据生成成功: ticket={}", ticket);
            return ticket;

        } catch (Exception e) {
            log.error("SSO登录失败: userId={}", userId, e);
            throw new RuntimeException("SSO登录失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> validateTicket(String ticket, String service) {
        log.info("SSO票据验证: ticket={}, service={}", ticket, service);

        Map<String, Object> result = new HashMap<>();

        try {
            // 获取票据信息
            String ticketData = stringRedisTemplate.opsForValue().get(SSO_TICKET_KEY + ticket);
            if (ticketData == null) {
                result.put("valid", false);
                result.put("message", "票据无效或已过期");
                return result;
            }

            // 票据验证通过，立即删除（一次性使用）
            stringRedisTemplate.delete(SSO_TICKET_KEY + ticket);

            // 解析票据信息（简化处理，实际应该用JSON）
            // 这里应该解析存储的用户ID和服务信息
            // 为了演示，假设能获取到userId
            Long userId = extractUserIdFromTicket(ticketData);

            if (userId != null) {
                // 生成SSO Token
                String ssoToken = "SSO-" + UUID.randomUUID().toString().replace("-", "");

                // 存储SSO Token信息
                Map<String, Object> tokenInfo = new HashMap<>();
                tokenInfo.put("userId", userId);
                tokenInfo.put("service", service);
                tokenInfo.put("loginTime", LocalDateTime.now().toString());

                stringRedisTemplate.opsForValue().set(
                        SSO_TOKEN_KEY + ssoToken,
                        tokenInfo.toString(),
                        SSO_TOKEN_EXPIRE_HOURS,
                        TimeUnit.HOURS
                );

                // 获取用户信息
                userService.findById(userId).ifPresent(user -> {
                    result.put("username", user.getUsername());
                    result.put("nickname", user.getNickname());
                    result.put("email", user.getEmail());
                });

                result.put("valid", true);
                result.put("userId", userId);
                result.put("ssoToken", ssoToken);
                result.put("message", "票据验证成功");

                log.info("SSO票据验证成功: userId={}", userId);
            } else {
                result.put("valid", false);
                result.put("message", "票据数据解析失败");
            }

        } catch (Exception e) {
            log.error("SSO票据验证失败: ticket={}", ticket, e);
            result.put("valid", false);
            result.put("message", "票据验证异常: " + e.getMessage());
        }

        return result;
    }

    @Override
    public boolean ssoLogout(String token) {
        log.info("SSO统一登出: token={}", token);

        try {
            // 删除SSO Token
            boolean deleted = Boolean.TRUE.equals(stringRedisTemplate.delete(SSO_TOKEN_KEY + token));

            if (deleted) {
                log.info("SSO登出成功: token={}", token);
                // 这里可以通知其他服务用户已登出
                notifyServicesLogout(token);
            }

            return deleted;

        } catch (Exception e) {
            log.error("SSO登出失败: token={}", token, e);
            return false;
        }
    }

    @Override
    public boolean checkSsoLogin(String ssoToken) {
        try {
            String tokenData = stringRedisTemplate.opsForValue().get(SSO_TOKEN_KEY + ssoToken);
            return tokenData != null;
        } catch (Exception e) {
            log.debug("SSO登录状态检查失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Map<String, Object> getSsoUserInfo(String ssoToken) {
        Map<String, Object> userInfo = new HashMap<>();

        try {
            String tokenData = stringRedisTemplate.opsForValue().get(SSO_TOKEN_KEY + ssoToken);
            if (tokenData != null) {
                // 解析Token数据获取用户ID
                Long userId = extractUserIdFromToken(tokenData);
                if (userId != null) {
                    userService.findById(userId).ifPresent(user -> {
                        userInfo.put("userId", user.getUserId());
                        userInfo.put("username", user.getUsername());
                        userInfo.put("nickname", user.getNickname());
                        userInfo.put("email", user.getEmail());
                        userInfo.put("status", user.getStatus());
                        userInfo.put("roles", userService.getUserRoles(userId));
                        userInfo.put("permissions", userService.getUserPermissions(userId));
                    });
                }
            }
        } catch (Exception e) {
            log.error("获取SSO用户信息失败: {}", e.getMessage());
        }

        return userInfo;
    }

    @Override
    public String generateServiceToken(String fromService, String toService) {
        try {
            String serviceToken = "SVC-" + UUID.randomUUID().toString().replace("-", "");

            Map<String, Object> tokenInfo = new HashMap<>();
            tokenInfo.put("fromService", fromService);
            tokenInfo.put("toService", toService);
            tokenInfo.put("createTime", LocalDateTime.now().toString());

            stringRedisTemplate.opsForValue().set(
                    SERVICE_TOKEN_KEY + serviceToken,
                    tokenInfo.toString(),
                    SERVICE_TOKEN_EXPIRE_MINUTES,
                    TimeUnit.MINUTES
            );

            log.info("服务间Token生成成功: from={}, to={}", fromService, toService);
            return serviceToken;

        } catch (Exception e) {
            log.error("服务间Token生成失败: from={}, to={}", fromService, toService, e);
            return null;
        }
    }

    @Override
    public boolean validateServiceToken(String serviceToken, String fromService, String toService) {
        try {
            String tokenData = stringRedisTemplate.opsForValue().get(SERVICE_TOKEN_KEY + serviceToken);
            if (tokenData != null) {
                // 简化验证，实际应该解析JSON验证服务信息
                stringRedisTemplate.delete(SERVICE_TOKEN_KEY + serviceToken); // 一次性使用
                return true;
            }
        } catch (Exception e) {
            log.error("服务间Token验证失败: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 从票据数据中提取用户ID（简化实现）
     */
    private Long extractUserIdFromTicket(String ticketData) {
        // 实际实现应该用JSON解析
        // 这里简化处理
        try {
            if (ticketData.contains("userId")) {
                // 简单的字符串解析，实际应该用JSON
                return 1L; // 示例返回
            }
        } catch (Exception e) {
            log.error("票据数据解析失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 从Token数据中提取用户ID（简化实现）
     */
    private Long extractUserIdFromToken(String tokenData) {
        // 同上，实际实现应该用JSON解析
        return 1L; // 示例返回
    }

    /**
     * 通知其他服务用户已登出
     */
    private void notifyServicesLogout(String token) {
        // 这里可以通过消息队列或HTTP通知其他服务
        log.info("通知其他服务用户登出: token={}", token);
    }
}