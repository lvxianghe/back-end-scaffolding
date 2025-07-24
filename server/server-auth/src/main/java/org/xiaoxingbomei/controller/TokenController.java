package org.xiaoxingbomei.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.constant.ApiConstant;
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
    @PostMapping(ApiConstant.Token.refreshToken)
    @Operation(summary = "刷新Token")
    public GlobalResponse<Map<String, Object>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        Map<String, Object> result = tokenService.refreshToken(refreshToken);
        return GlobalResponse.success(result);
    }

    /**
     * 验证Token
     */
    @PostMapping(ApiConstant.Token.validateToken)
    @Operation(summary = "验证Token", description = "供其他服务调用")
    public GlobalResponse<Boolean> validateToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        boolean valid = tokenService.validateToken(token);
        return GlobalResponse.success(valid);
    }

    /**
     * 获取Token信息
     */
    @PostMapping(ApiConstant.Token.getTokenInfo)
    @Operation(summary = "获取Token信息", description = "供其他服务调用")
    public GlobalResponse<Map<String, Object>> getTokenInfo(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        Map<String, Object> info = tokenService.getTokenInfo(token);
        return GlobalResponse.success(info);
    }

    /**
     * 强制用户下线
     */
    @PostMapping(ApiConstant.Token.kickOut)
    @Operation(summary = "强制用户下线")
    public GlobalResponse<Boolean> kickOut(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        boolean success = tokenService.kickOut(userId);
        return GlobalResponse.success(success);
    }

    /**
     * 批量强制用户下线
     */
    @PostMapping(ApiConstant.Token.batchKickOut)
    @Operation(summary = "批量强制用户下线")
    public GlobalResponse<Boolean> batchKickOut(@RequestBody Map<String, List<Long>> request) {
        List<Long> userIds = request.get("userIds");
        boolean success = tokenService.batchKickOut(userIds);
        return GlobalResponse.success(success);
    }

    /**
     * 获取在线用户列表
     */
    @PostMapping(ApiConstant.Token.getOnlineUsers)
    @Operation(summary = "获取在线用户列表")
    public GlobalResponse<List<Map<String, Object>>> getOnlineUsers() {
        List<Map<String, Object>> users = tokenService.getOnlineUsers();
        return GlobalResponse.success(users);
    }

    /**
     * 将Token加入黑名单
     */
    @PostMapping(ApiConstant.Token.addToBlacklist)
    @Operation(summary = "将Token加入黑名单")
    public GlobalResponse<Boolean> addToBlacklist(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        boolean success = tokenService.addToBlacklist(token);
        return GlobalResponse.success(success);
    }

    // ================================ SSO接口 ================================

    /**
     * SSO登录 - 生成票据
     */
    @PostMapping(ApiConstant.Token.ssoLogin)
    @Operation(summary = "SSO登录", description = "生成SSO票据")
    public GlobalResponse<String> ssoLogin(@RequestBody Map<String, Object> request) {
        Long userId = ((Number) request.get("userId")).longValue();
        String service = (String) request.get("service");
        String ticket = ssoService.ssoLogin(userId, service);
        return GlobalResponse.success(ticket);
    }

    /**
     * SSO票据验证
     */
    @PostMapping(ApiConstant.Token.validateTicket)
    @Operation(summary = "SSO票据验证", description = "验证SSO票据并获取用户信息")
    public GlobalResponse<Map<String, Object>> validateTicket(@RequestBody Map<String, String> request) {
        String ticket = request.get("ticket");
        String service = request.get("service");
        Map<String, Object> result = ssoService.validateTicket(ticket, service);
        return GlobalResponse.success(result);
    }

    /**
     * SSO统一登出
     */
    @PostMapping(ApiConstant.Token.ssoLogout)
    @Operation(summary = "SSO统一登出")
    public GlobalResponse<Boolean> ssoLogout(@RequestBody Map<String, String> request) {
        String ssoToken = request.get("ssoToken");
        boolean success = ssoService.ssoLogout(ssoToken);
        return GlobalResponse.success(success);
    }

    /**
     * 检查SSO登录状态
     */
    @PostMapping(ApiConstant.Token.checkSsoLogin)
    @Operation(summary = "检查SSO登录状态", description = "供其他服务调用")
    public GlobalResponse<Boolean> checkSsoLogin(@RequestBody Map<String, String> request) {
        String ssoToken = request.get("ssoToken");
        boolean loginStatus = ssoService.checkSsoLogin(ssoToken);
        return GlobalResponse.success(loginStatus);
    }

    /**
     * 获取SSO用户信息
     */
    @PostMapping(ApiConstant.Token.getSsoUserInfo)
    @Operation(summary = "获取SSO用户信息", description = "供其他服务调用")
    public GlobalResponse<Map<String, Object>> getSsoUserInfo(@RequestBody Map<String, String> request) {
        String ssoToken = request.get("ssoToken");
        Map<String, Object> userInfo = ssoService.getSsoUserInfo(ssoToken);
        return GlobalResponse.success(userInfo);
    }

    // ================================ 服务间Token接口 ================================

    /**
     * 生成服务间Token
     */
    @PostMapping(ApiConstant.Token.generateServiceToken)
    @Operation(summary = "生成服务间Token", description = "供微服务间调用")
    public GlobalResponse<String> generateServiceToken(@RequestBody Map<String, String> request) {
        String fromService = request.get("fromService");
        String toService = request.get("toService");
        String serviceToken = ssoService.generateServiceToken(fromService, toService);
        return GlobalResponse.success(serviceToken);
    }

    /**
     * 验证服务间Token
     */
    @PostMapping(ApiConstant.Token.validateServiceToken)
    @Operation(summary = "验证服务间Token", description = "供微服务间调用")
    public GlobalResponse<Boolean> validateServiceToken(@RequestBody Map<String, String> request) {
        String serviceToken = request.get("serviceToken");
        String fromService = request.get("fromService");
        String toService = request.get("toService");
        boolean valid = ssoService.validateServiceToken(serviceToken, fromService, toService);
        return GlobalResponse.success(valid);
    }
}