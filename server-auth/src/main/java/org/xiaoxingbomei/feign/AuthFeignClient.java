package org.xiaoxingbomei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;

import java.util.List;
import java.util.Map;

/**
 * Auth服务Feign客户端 - 统一接口
 */
@FeignClient(name = "auth-service", url = "${auth.service.url:http://localhost:10002}")
public interface AuthFeignClient
{

    // ================================ 认证相关接口 ================================

    @PostMapping("/auth/login")
    GlobalResponse<Map<String, Object>> login(@RequestBody Object loginRequest);

    @PostMapping("/auth/logout")
    GlobalResponse<Void> logout();

    @GetMapping("/auth/userInfo")
    GlobalResponse<Object> getCurrentUserInfo();

    // ================================ 服务间调用接口 ================================

    @GetMapping("/auth/permissions/{userId}")
    GlobalResponse<List<String>> getUserPermissions(@PathVariable("userId") Long userId);

    @GetMapping("/auth/roles/{userId}")
    GlobalResponse<List<String>> getUserRoles(@PathVariable("userId") Long userId);

    @PostMapping("/auth/verify")
    GlobalResponse<Boolean> verifyPassword(@RequestParam("username") String username,
                                           @RequestParam("password") String password);

    @GetMapping("/auth/user/{userId}")
    GlobalResponse<Object> getUserById(@PathVariable("userId") Long userId);

    // ================================ 用户管理接口 ================================

    @GetMapping("/auth/users")
    GlobalResponse<List<Object>> listUsers();

    @PostMapping("/auth/users")
    GlobalResponse<Object> createUser(@RequestBody Object createUserRequest);

    @PutMapping("/auth/users/{userId}")
    GlobalResponse<Object> updateUser(@PathVariable("userId") Long userId, @RequestBody Object user);

    @DeleteMapping("/auth/users/{userId}")
    GlobalResponse<Void> deleteUser(@PathVariable("userId") Long userId);


}