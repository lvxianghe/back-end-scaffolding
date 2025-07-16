package org.xiaoxingbomei.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.common.utils.IdGenerate_Utils;
import org.xiaoxingbomei.constant.ApiConstant;
import org.xiaoxingbomei.dto.auth.CreateUserRequest;
import org.xiaoxingbomei.dto.auth.LoginRequest;
import org.xiaoxingbomei.dto.auth.UserInfoResponse;
import org.xiaoxingbomei.entity.auth.SysUser;
import org.xiaoxingbomei.service.auth.AuthService;
import org.xiaoxingbomei.service.auth.UserService;

import java.util.List;
import java.util.Map;

/**
 * 统一认证控制器
 * 包含用户认证、用户管理、权限查询等功能
 *
 * @author xiaoxingbomei
 * @date 2024-01-01
 */
@RestController
@Slf4j
@Tag(name = "认证管理", description = "用户认证和用户管理")
public class AuthController
{

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;


    // ================================ 认证相关接口 ================================

    /**
     * 用户登录
     */
    @PostMapping(value = ApiConstant.Auth.login)
    @Operation(summary = "用户登录", description = "验证用户名密码并返回Token")
    public GlobalResponse<Map<String, Object>> login(@Validated @RequestBody LoginRequest request)
    {
        Map<String, Object> result = authService.login(request);
        return GlobalResponse.success(result, "用户登录成功");
    }

    /**
     * 用户登出
     */
    @PostMapping(ApiConstant.Auth.logout)
    @Operation(summary = "用户登出", description = "清除用户会话")
    public GlobalResponse<Void> logout()
    {
        authService.logout();
        return GlobalResponse.success("用户登出成功");
    }

    /**
     * 检查登录状态
     */
    @PostMapping(ApiConstant.Auth.isLogin)
    @Operation(summary = "检查登录状态")
    public GlobalResponse<Boolean> isLogin() {
        boolean loginStatus = authService.isLogin();
        return GlobalResponse.success(loginStatus);
    }

    /**
     * 获取当前用户信息
     */
    @PostMapping(ApiConstant.Auth.userInfo)
    @Operation(summary = "获取当前用户信息")
    public GlobalResponse<UserInfoResponse> getUserInfo() {
        UserInfoResponse userInfo = authService.getCurrentUserInfo();
        return GlobalResponse.success(userInfo);
    }

    // ================================ 服务间调用接口 ================================

    /**
     * 根据用户ID获取用户信息 (供其他服务调用)
     */
    @PostMapping(ApiConstant.Auth.getUserById)
    @Operation(summary = "根据用户ID获取用户信息", description = "供其他服务调用")
    public GlobalResponse<UserInfoResponse> getUserById(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        UserInfoResponse userInfo = authService.getUserInfoById(userId);
        return GlobalResponse.success(userInfo);
    }

    /**
     * 获取用户权限列表 (供其他服务调用)
     */
    @PostMapping(ApiConstant.Auth.getUserPermissions)
    @Operation(summary = "获取用户权限列表", description = "供其他服务调用")
    public GlobalResponse<List<String>> getUserPermissions(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        List<String> permissions = authService.getUserPermissions(userId);
        return GlobalResponse.success(permissions);
    }

    /**
     * 获取用户角色列表 (供其他服务调用)
     */
    @PostMapping(ApiConstant.Auth.getUserRoles)
    @Operation(summary = "获取用户角色列表", description = "供其他服务调用")
    public GlobalResponse<List<String>> getUserRoles(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        List<String> roles = authService.getUserRoles(userId);
        return GlobalResponse.success(roles);
    }

    /**
     * 验证用户密码 (供其他服务调用)
     */
    @PostMapping(ApiConstant.Auth.verifyPassword)
    @Operation(summary = "验证用户密码", description = "供其他服务调用")
    public GlobalResponse<Boolean> verifyPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        boolean valid = authService.verifyPassword(username, password);
        return GlobalResponse.success(valid);
    }

    // ================================ 用户管理接口 ================================

    /**
     * 创建用户
     */
    @PostMapping(ApiConstant.Auth.createUser)
    @Operation(summary = "创建用户")
    public GlobalResponse<SysUser> createUser(@Validated @RequestBody CreateUserRequest request) {
        SysUser user = buildUserFromRequest(request);
        SysUser created = userService.createUser(user);
        return GlobalResponse.success(created);
    }

    /**
     * 获取用户列表
     */
    @PostMapping(ApiConstant.Auth.listUsers)
    @Operation(summary = "获取用户列表")
    public GlobalResponse<List<SysUser>> listUsers() {
        List<SysUser> users = userService.findAllUsers();
        return GlobalResponse.success(users);
    }

    /**
     * 根据ID获取用户详情
     */
    @PostMapping(ApiConstant.Auth.getUserDetailById)
    @Operation(summary = "根据ID获取用户详情")
    public GlobalResponse<SysUser> getUserDetailById(@RequestBody Map<String, Long> request)
    {
        Long userId = request.get("userId");
        return userService.findById(userId)
                .map(GlobalResponse::success)
                .orElse(GlobalResponse.error("用户不存在"));
    }

    /**
     * 更新用户
     */
    @PostMapping(ApiConstant.Auth.updateUser)
    @Operation(summary = "更新用户")
    public GlobalResponse<SysUser> updateUser(@RequestBody Map<String, Object> request) {
        Long userId = ((Number) request.get("userId")).longValue();
        SysUser user = (SysUser) request.get("user");
        user.setUserId(userId);
        SysUser updated = userService.updateUser(user);
        return GlobalResponse.success(updated);
    }

    /**
     * 删除用户
     */
    @PostMapping(ApiConstant.Auth.deleteUser)
    @Operation(summary = "删除用户")
    public GlobalResponse<Void> deleteUser(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        userService.deleteUser(userId);
        return GlobalResponse.success("用户删除成功");
    }

    /**
     * 重置用户密码
     */
    @PostMapping(ApiConstant.Auth.resetPassword)
    @Operation(summary = "重置用户密码")
    public GlobalResponse<Void> resetPassword(@RequestBody Map<String, Object> request) {
        Long userId = ((Number) request.get("userId")).longValue();
        String newPassword = (String) request.get("newPassword");
        boolean success = userService.resetPassword(userId, newPassword);
        return success ? GlobalResponse.success("重置密码成功") : GlobalResponse.error("重置密码失败");
    }

    // ================================ 私有方法 ================================

    /**
     * 构建用户对象
     */
    private SysUser buildUserFromRequest(CreateUserRequest request)
    {
        SysUser user = new SysUser();
        user.setUserId(new IdGenerate_Utils().nextId());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        return user;
    }
}