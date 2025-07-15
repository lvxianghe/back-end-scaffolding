package org.xiaoxingbomei.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.service.auth.SsoService;
import org.xiaoxingbomei.service.auth.TokenService;

import java.util.List;
import java.util.Map;

/**
 * Token管理控制器
 *
 * @author xiaoxingbomei
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/token")
@Tag(name = "Token管理", description = "Token生命周期和SSO管理")
@Slf4j
public class TokenController
{

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SsoService ssoService;

    // ================================ Token管理接口 ================================

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    @SaIgnore
    @Operation(summary = "刷新Token")
    public GlobalResponse<Map<String, Object>> refreshToken(@RequestParam String refreshToken) {
        Map<String, Object> result = tokenService.refreshToken(refreshToken);
        return GlobalResponse.success(result);
    }

    /**
     * 验证Token
     */
    @PostMapping("/validate")
    @SaIgnore
    @Operation(summary = "验证Token", description = "供其他服务调用")
    public GlobalResponse<Boolean> validateToken(@RequestParam String token) {
        boolean valid = tokenService.validateToken(token);
        return GlobalResponse.success(valid);
    }

    /**
     * 获取Token信息
     */
    @GetMapping("/info")
    @SaIgnore
    @Operation(summary = "获取Token信息", description = "供其他服务调用")
    public GlobalResponse<Map<String, Object>> getTokenInfo(@RequestParam String token) {
        Map<String, Object> info = tokenService.getTokenInfo(token);
        return GlobalResponse.success(info);
    }

    /**
     * 强制用户下线
     */
    @PostMapping("/kickout/{userId}")
    @SaCheckPermission("token:kickout")
    @Operation(summary = "强制用户下线")
    public GlobalResponse<Boolean> kickOut(@PathVariable Long userId) {
        boolean success = tokenService.kickOut(userId);
        return GlobalResponse.success(success);
    }

    /**
     * 批量强制用户下线
     */
    @PostMapping("/kickout/batch")
    @SaCheckPermission("token:kickout")
    @Operation(summary = "批量强制用户下线")
    public GlobalResponse<Boolean> batchKickOut(@RequestBody List<Long> userIds) {
        boolean success = tokenService.batchKickOut(userIds);
        return GlobalResponse.success(success);
    }

    /**
     * 获取在线用户列表
     */
    @GetMapping("/online")
    @SaCheckPermission("token:monitor")
    @Operation(summary = "获取在线用户列表")
    public GlobalResponse<List<Map<String, Object>>> getOnlineUsers() {
        List<Map<String, Object>> users = tokenService.getOnlineUsers();
        return GlobalResponse.success(users);
    }

    /**
     * 将Token加入黑名单
     */
    @PostMapping("/blacklist")
    @SaCheckPermission("token:blacklist")
    @Operation(summary = "将Token加入黑名单")
    public GlobalResponse<Boolean> addToBlacklist(@RequestParam String token) {
        boolean success = tokenService.addToBlacklist(token);
        return GlobalResponse.success(success);
    }

    // ================================ SSO接口 ================================

    /**
     * SSO登录 - 生成票据
     */
    @PostMapping("/sso/login")
    @SaIgnore
    @Operation(summary = "SSO登录", description = "生成SSO票据")
    public GlobalResponse<String> ssoLogin(@RequestParam Long userId, @RequestParam String service) {
        String ticket = ssoService.ssoLogin(userId, service);
        return GlobalResponse.success(ticket);
    }

    /**
     * SSO票据验证
     */
    @PostMapping("/sso/validate")
    @SaIgnore
    @Operation(summary = "SSO票据验证", description = "验证SSO票据并获取用户信息")
    public GlobalResponse<Map<String, Object>> validateTicket(@RequestParam String ticket, @RequestParam String service) {
        Map<String, Object> result = ssoService.validateTicket(ticket, service);
        return GlobalResponse.success(result);
    }

    /**
     * SSO统一登出
     */
    @PostMapping("/sso/logout")
    @SaIgnore
    @Operation(summary = "SSO统一登出")
    public GlobalResponse<Boolean> ssoLogout(@RequestParam String ssoToken) {
        boolean success = ssoService.ssoLogout(ssoToken);
        return GlobalResponse.success(success);
    }

    /**
     * 检查SSO登录状态
     */
    @GetMapping("/sso/status")
    @SaIgnore
    @Operation(summary = "检查SSO登录状态", description = "供其他服务调用")
    public GlobalResponse<Boolean> checkSsoLogin(@RequestParam String ssoToken) {
        boolean loginStatus = ssoService.checkSsoLogin(ssoToken);
        return GlobalResponse.success(loginStatus);
    }

    /**
     * 获取SSO用户信息
     */
    @GetMapping("/sso/userinfo")
    @SaIgnore
    @Operation(summary = "获取SSO用户信息", description = "供其他服务调用")
    public GlobalResponse<Map<String, Object>> getSsoUserInfo(@RequestParam String ssoToken) {
        Map<String, Object> userInfo = ssoService.getSsoUserInfo(ssoToken);
        return GlobalResponse.success(userInfo);
    }

    // ================================ 服务间Token接口 ================================

    /**
     * 生成服务间Token
     */
    @PostMapping("/service/generate")
    @SaIgnore
    @Operation(summary = "生成服务间Token", description = "供微服务间调用")
    public GlobalResponse<String> generateServiceToken(@RequestParam String fromService, @RequestParam String toService) {
        String serviceToken = ssoService.generateServiceToken(fromService, toService);
        return GlobalResponse.success(serviceToken);
    }

    /**
     * 验证服务间Token
     */
    @PostMapping("/service/validate")
    @SaIgnore
    @Operation(summary = "验证服务间Token", description = "供微服务间调用")
    public GlobalResponse<Boolean> validateServiceToken(@RequestParam String serviceToken,
                                                        @RequestParam String fromService,
                                                        @RequestParam String toService) {
        boolean valid = ssoService.validateServiceToken(serviceToken, fromService, toService);
        return GlobalResponse.success(valid);
    }
}