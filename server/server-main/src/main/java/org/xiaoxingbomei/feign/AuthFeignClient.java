package org.xiaoxingbomei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.constant.ApiConstant;

import java.util.List;
import java.util.Map;

/**
 * Auth服务Feign客户端
 * 
 * @author xiaoxingbomei
 * @date 2024-01-01
 */
@FeignClient(name = "auth-service", url = "${auth.service.url:http://localhost:10002}")
public interface AuthFeignClient
{

    /**
     * 获取用户权限列表
     */
    @PostMapping(ApiConstant.Auth.getUserPermissions)
    GlobalResponse<List<String>> getUserPermissions(@RequestBody Map<String, Long> request);

    /**
     * 获取用户角色列表
     */
    @PostMapping(ApiConstant.Auth.getUserRoles)
    GlobalResponse<List<String>> getUserRoles(@RequestBody Map<String, Long> request);

    /**
     * 验证用户密码
     */
    @PostMapping(ApiConstant.Auth.verifyPassword)
    GlobalResponse<Boolean> verifyPassword(@RequestBody Map<String, String> request);

    /**
     * 获取用户信息
     */
    @PostMapping(ApiConstant.Auth.getUserById)
    GlobalResponse<Object> getUserById(@RequestBody Map<String, Long> request);
} 