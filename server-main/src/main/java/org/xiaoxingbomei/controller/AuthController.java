package org.xiaoxingbomei.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.feign.AuthFeignClient;

import java.util.Map;

/**
 * 主服务认证控制器 - 主要处理登录入口
 * 实际认证逻辑由auth服务处理
 *
 * @author xiaoxingbomei
 * @date 2024-01-01
 */
@RestController
@Tag(name = "主服务认证", description = "登录入口和用户信息")
@Slf4j
public class AuthController
{

    @Autowired
    private AuthFeignClient authFeignClient;

    /**
     * 登录接口 - 转发到auth服务
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "转发到auth服务处理")
    public GlobalResponse<Map<String, Object>> login(@RequestBody Object loginRequest)
    {
        // 这里可以直接转发到auth服务，或者添加一些前置处理
        log.info("主服务收到登录请求，转发到auth服务");
        // 实际业务中可能需要记录登录日志等
        return GlobalResponse.success("请直接调用auth服务的登录接口: /auth/login");
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/userInfo")
    @SaCheckLogin
    @Operation(summary = "获取当前用户信息")
    public GlobalResponse<Object> getCurrentUserInfo()
    {
        // 通过Feign调用auth服务
        // 这里可以添加一些业务逻辑，比如用户行为记录等
        return GlobalResponse.success("请通过auth服务获取用户信息");
    }
}