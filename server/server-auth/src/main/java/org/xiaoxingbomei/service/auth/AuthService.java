package org.xiaoxingbomei.service.auth;

import org.xiaoxingbomei.dto.auth.LoginRequest;
import org.xiaoxingbomei.dto.auth.UserInfoResponse;

import java.util.List;
import java.util.Map;

/**
 * 认证服务接口
 * 
 * @author xiaoxingbomei
 * @date 2024-01-01
 */
public interface AuthService
{

    /**
     * 用户登录
     */
    Map<String, Object> login(LoginRequest request);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 检查登录状态
     */
    boolean isLogin();

    /**
     * 获取当前用户信息
     */
    UserInfoResponse getCurrentUserInfo();

    /**
     * 根据用户ID获取用户信息
     */
    UserInfoResponse getUserInfoById(Long userId);

    /**
     * 获取用户权限列表
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 获取用户角色列表
     */
    List<String> getUserRoles(Long userId);

    /**
     * 验证用户密码
     */
    boolean verifyPassword(String username, String password);

}