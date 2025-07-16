package org.xiaoxingbomei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.xiaoxingbomei.constant.ApiConstant;

import java.util.List;
import java.util.Map;

/**
 * Auth服务Feign客户端 - 统一接口
 */
@FeignClient(name = "auth-service", url = "${auth.service.url:http://localhost:10002}")
public interface AuthFeignClient
{

    // ================================ 认证相关接口 ================================

    @PostMapping(ApiConstant.Auth.login)
    GlobalResponse<Map<String, Object>> login(@RequestBody Object loginRequest);

    @PostMapping(ApiConstant.Auth.logout)
    GlobalResponse<Void> logout();

    @PostMapping(ApiConstant.Auth.userInfo)
    GlobalResponse<Object> getCurrentUserInfo();

    // ================================ 服务间调用接口 ================================

    @PostMapping(ApiConstant.Auth.getUserPermissions)
    GlobalResponse<List<String>> getUserPermissions(@RequestBody Map<String, Long> request);

    @PostMapping(ApiConstant.Auth.getUserRoles)
    GlobalResponse<List<String>> getUserRoles(@RequestBody Map<String, Long> request);

    @PostMapping(ApiConstant.Auth.verifyPassword)
    GlobalResponse<Boolean> verifyPassword(@RequestBody Map<String, String> request);

    @PostMapping(ApiConstant.Auth.getUserById)
    GlobalResponse<Object> getUserById(@RequestBody Map<String, Long> request);

    // ================================ 用户管理接口 ================================

    @PostMapping(ApiConstant.Auth.listUsers)
    GlobalResponse<List<Object>> listUsers();

    @PostMapping(ApiConstant.Auth.createUser)
    GlobalResponse<Object> createUser(@RequestBody Object createUserRequest);

    @PostMapping(ApiConstant.Auth.updateUser)
    GlobalResponse<Object> updateUser(@RequestBody Map<String, Object> request);

    @PostMapping(ApiConstant.Auth.deleteUser)
    GlobalResponse<Void> deleteUser(@RequestBody Map<String, Long> request);

}