package org.xiaoxingbomei.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoxingbomei.dto.auth.LoginRequest;
import org.xiaoxingbomei.dto.auth.UserInfoResponse;
import org.xiaoxingbomei.entity.auth.SysUser;
import org.xiaoxingbomei.service.auth.AuthService;
import org.xiaoxingbomei.service.auth.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * RBAC-认证服务实现类
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService
{

    @Autowired
    private UserService userService;

    @Override
    public Map<String, Object> login(LoginRequest request)
    {
        log.info("用户登录请求: {}", request.getUsername());

        // 验证用户名密码
        if (!userService.validatePassword(request.getUsername(), request.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 获取用户信息
        Optional<SysUser> userOpt = userService.findByUsername(request.getUsername());
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }

        SysUser user = userOpt.get();

        // 执行登录
        StpUtil.login(user.getUserId());

        // 返回登录信息
        Map<String, Object> result = new HashMap<>();
        result.put("token", StpUtil.getTokenValue());
        result.put("userId", user.getUserId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());

        log.info("用户登录成功: userId={}", user.getUserId());
        return result;
    }

    @Override
    public void logout()
    {
        String userId = StpUtil.getLoginIdAsString();
        StpUtil.logout();
        log.info("用户登出成功: userId={}", userId);
    }

    @Override
    public boolean isLogin()
    {
        return StpUtil.isLogin();
    }

    @Override
    public UserInfoResponse getCurrentUserInfo()
    {
        Long userId = StpUtil.getLoginIdAsLong();
        return getUserInfoById(userId);
    }

    @Override
    public UserInfoResponse getUserInfoById(Long userId)
    {
        Optional<SysUser> userOpt = userService.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在: " + userId);
        }

        SysUser user = userOpt.get();
        UserInfoResponse response = new UserInfoResponse();
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setAvatar(user.getAvatar());
        response.setStatus(user.getStatus());
        response.setCreateTime(user.getCreateTime());
        response.setRoles(userService.getUserRoles(userId));
        response.setPermissions(userService.getUserPermissions(userId));

        return response;
    }

    @Override
    public List<String> getUserPermissions(Long userId)
    {
        return userService.getUserPermissions(userId);
    }

    @Override
    public List<String> getUserRoles(Long userId)
    {
        return userService.getUserRoles(userId);
    }

    @Override
    public boolean verifyPassword(String username, String password)
    {
        return userService.validatePassword(username, password);
    }

}