package org.xiaoxingbomei.service.impl;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.xiaoxingbomei.service.auth.TokenService;
import org.xiaoxingbomei.service.auth.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Token生命周期管理服务实现
 *
 * @author xiaoxingbomei
 * @date 2024-01-01
 */
@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Autowired
    private SaTokenDao saTokenDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserService userService;

    private static final String TOKEN_BLACKLIST_KEY = "auth:token:blacklist";
    private static final String REFRESH_TOKEN_KEY = "auth:refresh:token:";
    private static final String ONLINE_USERS_KEY = "auth:online:users";

    @Override
    public Map<String, Object> refreshToken(String refreshToken) {
        log.info("刷新Token请求: {}", refreshToken);

        try {
            // 验证刷新Token
            String userIdStr = stringRedisTemplate.opsForValue().get(REFRESH_TOKEN_KEY + refreshToken);
            if (userIdStr == null) {
                throw new RuntimeException("刷新Token无效或已过期");
            }

            Long userId = Long.valueOf(userIdStr);

            // 生成新的访问Token
            StpUtil.login(userId);
            String newToken = StpUtil.getTokenValue();

            // 生成新的刷新Token
            String newRefreshToken = UUID.randomUUID().toString().replace("-", "");
            stringRedisTemplate.opsForValue().set(
                    REFRESH_TOKEN_KEY + newRefreshToken,
                    userIdStr,
                    7, TimeUnit.DAYS // 刷新Token有效期7天
            );

            // 删除旧的刷新Token
            stringRedisTemplate.delete(REFRESH_TOKEN_KEY + refreshToken);

            Map<String, Object> result = new HashMap<>();
            result.put("accessToken", newToken);
            result.put("refreshToken", newRefreshToken);
            result.put("expiresIn", StpUtil.getTokenTimeout());
            result.put("refreshTime", LocalDateTime.now());

            log.info("Token刷新成功: userId={}", userId);
            return result;

        } catch (Exception e) {
            log.error("Token刷新失败: {}", e.getMessage());
            throw new RuntimeException("Token刷新失败: " + e.getMessage());
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            // 检查Token是否在黑名单
            if (isInBlacklist(token)) {
                return false;
            }

            // 使用Sa-Token验证
            Object loginId = StpUtil.getLoginIdByToken(token);
            return loginId != null;

        } catch (Exception e) {
            log.debug("Token验证失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Map<String, Object> getTokenInfo(String token) {
        Map<String, Object> info = new HashMap<>();

        try {
            Object loginId = StpUtil.getLoginIdByToken(token);
            if (loginId != null) {
                Long userId = Long.valueOf(loginId.toString());

                info.put("valid", true);
                info.put("userId", userId);
                info.put("timeout", StpUtil.getTokenTimeout(token));
                info.put("createTime", StpUtil.getTokenSessionByToken(token).getCreateTime());

                // 获取用户基本信息
                userService.findById(userId).ifPresent(user -> {
                    info.put("username", user.getUsername());
                    info.put("nickname", user.getNickname());
                });

            } else {
                info.put("valid", false);
                info.put("reason", "Token无效");
            }

        } catch (Exception e) {
            info.put("valid", false);
            info.put("reason", e.getMessage());
        }

        return info;
    }

    @Override
    public boolean kickOut(Long userId) {
        try {
            StpUtil.kickout(userId);
            log.info("强制用户下线成功: userId={}", userId);
            return true;
        } catch (Exception e) {
            log.error("强制用户下线失败: userId={}", userId, e);
            return false;
        }
    }

    @Override
    public boolean batchKickOut(List<Long> userIds) {
        int successCount = 0;
        for (Long userId : userIds) {
            if (kickOut(userId)) {
                successCount++;
            }
        }
        log.info("批量强制下线完成: 成功{}/总计{}", successCount, userIds.size());
        return successCount == userIds.size();
    }

    @Override
    public boolean addToBlacklist(String token) {
        try {
            long timeout = StpUtil.getTokenTimeout(token);
            if (timeout > 0) {
                stringRedisTemplate.opsForSet().add(TOKEN_BLACKLIST_KEY, token);
                stringRedisTemplate.expire(TOKEN_BLACKLIST_KEY, timeout, TimeUnit.SECONDS);
                log.info("Token已加入黑名单: {}", token);
                return true;
            }
        } catch (Exception e) {
            log.error("Token加入黑名单失败: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean isInBlacklist(String token) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(TOKEN_BLACKLIST_KEY, token));
    }

    @Override
    public List<Map<String, Object>> getOnlineUsers() {
        List<Map<String, Object>> onlineUsers = new ArrayList<>();

        try {
            // 获取所有在线Token
            List<String> tokenList = StpUtil.searchTokenValue("", 0, -1, false);

            for (String token : tokenList) {
                try {
                    Object loginId = StpUtil.getLoginIdByToken(token);
                    if (loginId != null) {
                        Long userId = Long.valueOf(loginId.toString());

                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("userId", userId);
                        userInfo.put("token", token);
                        userInfo.put("loginTime", StpUtil.getTokenSessionByToken(token).getCreateTime());
                        userInfo.put("lastActiveTime", new Date());

                        // 获取用户详细信息
                        userService.findById(userId).ifPresent(user -> {
                            userInfo.put("username", user.getUsername());
                            userInfo.put("nickname", user.getNickname());
                        });

                        onlineUsers.add(userInfo);
                    }
                } catch (Exception e) {
                    log.debug("获取在线用户信息失败: {}", e.getMessage());
                }
            }

        } catch (Exception e) {
            log.error("获取在线用户列表失败: {}", e.getMessage());
        }

        return onlineUsers;
    }

    @Override
    public List<String> getUserTokens(Long userId) {
        try {
            return StpUtil.getTokenValueListByLoginId(userId);
        } catch (Exception e) {
            log.error("获取用户Token列表失败: userId={}", userId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public void cleanExpiredTokens() {
        log.info("开始清理过期Token...");
        try {
            // Sa-Token会自动清理过期Token，这里主要清理黑名单中的过期Token
            Set<String> blacklistTokens = stringRedisTemplate.opsForSet().members(TOKEN_BLACKLIST_KEY);
            if (blacklistTokens != null) {
                for (String token : blacklistTokens) {
                    if (!validateToken(token)) {
                        stringRedisTemplate.opsForSet().remove(TOKEN_BLACKLIST_KEY, token);
                    }
                }
            }
            log.info("Token清理完成");
        } catch (Exception e) {
            log.error("Token清理失败: {}", e.getMessage());
        }
    }

    @Override
    public boolean extendTokenExpiration(String token, long seconds) {
        try {
            Object loginId = StpUtil.getLoginIdByToken(token);
            if (loginId != null) {
                StpUtil.renewTimeout(token, seconds);
                log.info("Token有效期延长成功: token={}, seconds={}", token, seconds);
                return true;
            }
        } catch (Exception e) {
            log.error("Token有效期延长失败: {}", e.getMessage());
        }
        return false;
    }
}