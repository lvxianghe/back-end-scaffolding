package org.xiaoxingbomei.service.auth;

import java.util.Map;

/**
 * SSO单点登录服务
 */
public interface SsoService
{

    /**
     * SSO登录 - 生成票据
     */
    String ssoLogin(Long userId, String service);

    /**
     * SSO票据验证
     */
    Map<String, Object> validateTicket(String ticket, String service);

    /**
     * SSO统一登出
     */
    boolean ssoLogout(String token);

    /**
     * 检查SSO登录状态
     */
    boolean checkSsoLogin(String ssoToken);

    /**
     * 获取SSO用户信息
     */
    Map<String, Object> getSsoUserInfo(String ssoToken);

    /**
     * 生成服务间认证Token
     */
    String generateServiceToken(String fromService, String toService);

    /**
     * 验证服务间Token
     */
    boolean validateServiceToken(String serviceToken, String fromService, String toService);
}