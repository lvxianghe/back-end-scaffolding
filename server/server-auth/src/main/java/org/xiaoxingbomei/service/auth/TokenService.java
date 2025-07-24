package org.xiaoxingbomei.service.auth;

import java.util.List;
import java.util.Map;

/**
 * Token生命周期管理服务
 */
public interface TokenService
{

    /**
     * 刷新Token
     */
    Map<String, Object> refreshToken(String refreshToken);

    /**
     * 验证Token有效性
     */
    boolean validateToken(String token);

    /**
     * 获取Token信息
     */
    Map<String, Object> getTokenInfo(String token);

    /**
     * 强制用户下线
     */
    boolean kickOut(Long userId);

    /**
     * 批量强制用户下线
     */
    boolean batchKickOut(List<Long> userIds);

    /**
     * 将Token加入黑名单
     */
    boolean addToBlacklist(String token);

    /**
     * 检查Token是否在黑名单
     */
    boolean isInBlacklist(String token);

    /**
     * 获取在线用户列表
     */
    List<Map<String, Object>> getOnlineUsers();

    /**
     * 获取用户所有Token
     */
    List<String> getUserTokens(Long userId);

    /**
     * 清理过期Token
     */
    void cleanExpiredTokens();

    /**
     * 延长Token有效期
     */
    boolean extendTokenExpiration(String token, long seconds);
}