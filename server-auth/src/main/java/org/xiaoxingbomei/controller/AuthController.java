package org.xiaoxingbomei.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.common.utils.IdGenerate_Utils;
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
    @PostMapping("/login")
    @SaIgnore
    @Operation(summary = "用户登录", description = "验证用户名密码并返回Token")
    public GlobalResponse<Map<String, Object>> login(@Validated @RequestBody LoginRequest request) {
        Map<String, Object> result = authService.login(request);
        return GlobalResponse.success(result);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "清除用户会话")
    public GlobalResponse<Void> logout() {
        authService.logout();
        return GlobalResponse.success("用户登出成功");
    }

    /**
     * 检查登录状态
     */
    @GetMapping("/isLogin")
    @SaIgnore
    @Operation(summary = "检查登录状态")
    public GlobalResponse<Boolean> isLogin() {
        boolean loginStatus = authService.isLogin();
        return GlobalResponse.success(loginStatus);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/userInfo")
    @Operation(summary = "获取当前用户信息")
    public GlobalResponse<UserInfoResponse> getUserInfo() {
        UserInfoResponse userInfo = authService.getCurrentUserInfo();
        return GlobalResponse.success(userInfo);
    }

    // ================================ 服务间调用接口 ================================

    /**
     * 根据用户ID获取用户信息 (供其他服务调用)
     */
    @GetMapping("/user/{userId}")
    @SaIgnore
    @Operation(summary = "根据用户ID获取用户信息", description = "供其他服务调用")
    public GlobalResponse<UserInfoResponse> getUserById(@PathVariable Long userId) {
        UserInfoResponse userInfo = authService.getUserInfoById(userId);
        return GlobalResponse.success(userInfo);
    }

    /**
     * 获取用户权限列表 (供其他服务调用)
     */
    @GetMapping("/permissions/{userId}")
    @SaIgnore
    @Operation(summary = "获取用户权限列表", description = "供其他服务调用")
    public GlobalResponse<List<String>> getUserPermissions(@PathVariable Long userId) {
        List<String> permissions = authService.getUserPermissions(userId);
        return GlobalResponse.success(permissions);
    }

    /**
     * 获取用户角色列表 (供其他服务调用)
     */
    @GetMapping("/roles/{userId}")
    @SaIgnore
    @Operation(summary = "获取用户角色列表", description = "供其他服务调用")
    public GlobalResponse<List<String>> getUserRoles(@PathVariable Long userId) {
        List<String> roles = authService.getUserRoles(userId);
        return GlobalResponse.success(roles);
    }

    /**
     * 验证用户密码 (供其他服务调用)
     */
    @PostMapping("/verify")
    @SaIgnore
    @Operation(summary = "验证用户密码", description = "供其他服务调用")
    public GlobalResponse<Boolean> verifyPassword(@RequestParam String username, @RequestParam String password) {
        boolean valid = authService.verifyPassword(username, password);
        return GlobalResponse.success(valid);
    }

    // ================================ 用户管理接口 ================================

    /**
     * 创建用户
     */
    @PostMapping("/users")
    @SaCheckPermission("user:create")
    @Operation(summary = "创建用户")
    public GlobalResponse<SysUser> createUser(@Validated @RequestBody CreateUserRequest request) {
        SysUser user = buildUserFromRequest(request);
        SysUser created = userService.createUser(user);
        return GlobalResponse.success(created);
    }

    /**
     * 获取用户列表
     */
    @GetMapping("/users")
    @SaCheckPermission("user:read")
    @Operation(summary = "获取用户列表")
    public GlobalResponse<List<SysUser>> listUsers() {
        List<SysUser> users = userService.findAllUsers();
        return GlobalResponse.success(users);
    }

    /**
     * 根据ID获取用户详情
     */
    @GetMapping("/users/{userId}")
    @SaCheckPermission("user:read")
    @Operation(summary = "根据ID获取用户详情")
    public GlobalResponse<SysUser> getUserDetailById(@PathVariable Long userId)
    {
        return userService.findById(userId)
                .map(GlobalResponse::success)
                .orElse(GlobalResponse.error("用户不存在"));
    }

    /**
     * 更新用户
     */
    @PutMapping("/users/{userId}")
    @SaCheckPermission("user:update")
    @Operation(summary = "更新用户")
    public GlobalResponse<SysUser> updateUser(@PathVariable Long userId, @RequestBody SysUser user) {
        user.setUserId(userId);
        SysUser updated = userService.updateUser(user);
        return GlobalResponse.success(updated);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/users/{userId}")
    @SaCheckPermission("user:delete")
    @Operation(summary = "删除用户")
    public GlobalResponse<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return GlobalResponse.success("用户删除成功");
    }

    /**
     * 重置用户密码
     */
    @PutMapping("/users/{userId}/resetPassword")
    @SaCheckPermission("user:resetPassword")
    @Operation(summary = "重置用户密码")
    public GlobalResponse<Void> resetPassword(@PathVariable Long userId, @RequestParam String newPassword) {
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