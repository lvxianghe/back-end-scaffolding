package org.xiaoxingbomei.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.xiaoxingbomei.common.entity.SystemEntity;
import org.xiaoxingbomei.common.entity.response.GlobalResponse;
import org.redisson.spring.data.connection.RedissonConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统管理控制器
 */
@RestController
@RequestMapping("/api/system")
@Tag(name = "系统管理", description = "系统信息管理相关接口")
public class SystemController {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/info")
    @Operation(summary = "获取系统信息", description = "获取当前系统的基本信息")
    public GlobalResponse<SystemEntity> getSystemInfo() {
        SystemEntity system = new SystemEntity();
        system.setSystemNumber("SYS001");
        system.setSystemChineseName("后端脚手架系统");
        system.setSystemEnglishName("Backend Scaffolding System");
        system.setSystemVersion("1.0.0");
        system.setSystemDescription("基于SpringBoot + SpringCloud的后端脚手架项目");
        system.setSystemStatus("1");
        
        return GlobalResponse.success(system);
    }

    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查系统健康状态")
    public GlobalResponse<String> health() {
        return GlobalResponse.success("系统运行正常");
    }

    @PostMapping("/update")
    @Operation(summary = "更新系统信息", description = "更新系统的基本信息")
    public GlobalResponse<String> updateSystemInfo(
            @Parameter(description = "系统信息", required = true)
            @RequestBody SystemEntity systemEntity) {
        // 这里可以添加实际的更新逻辑
        return GlobalResponse.success("系统信息更新成功");
    }
    
    @GetMapping("/redis-info")
    @Operation(summary = "Redis配置信息", description = "获取当前Redis客户端类型和配置信息")
    public GlobalResponse<Map<String, Object>> getRedisInfo() {
        Map<String, Object> redisInfo = new HashMap<>();
        
        try {
            // 检查Redis连接工厂类型
            String clientType = "unknown";
            String factoryClass = redisConnectionFactory.getClass().getName();
            
            if (redisConnectionFactory instanceof LettuceConnectionFactory) {
                clientType = "Lettuce";
                LettuceConnectionFactory lettuceFactory = (LettuceConnectionFactory) redisConnectionFactory;
                redisInfo.put("host", lettuceFactory.getHostName());
                redisInfo.put("port", lettuceFactory.getPort());
                redisInfo.put("database", lettuceFactory.getDatabase());
                redisInfo.put("timeout", lettuceFactory.getTimeout());
            } else if (factoryClass.contains("RedissonConnectionFactory")) {
                clientType = "Redisson";
                redisInfo.put("description", "RedisTemplate使用Redisson作为底层客户端");
                redisInfo.put("advantage", "支持基础操作 + 分布式功能");
            } else {
                clientType = "其他客户端";
            }
            
            redisInfo.put("clientType", clientType);
            redisInfo.put("factoryClass", factoryClass);
            redisInfo.put("status", "连接正常");
            
            // 测试Redis连接
            String testKey = "test:connection:" + System.currentTimeMillis();
            String expectedValue = "success";
            redisTemplate.opsForValue().set(testKey, expectedValue);
            String actualValue = (String) redisTemplate.opsForValue().get(testKey);
            
            boolean testPassed = expectedValue.equals(actualValue);
            redisInfo.put("connectionTest", testPassed ? "通过" : "失败");
            redisInfo.put("testDetails", "期望值: " + expectedValue + ", 实际值: " + actualValue);
            
            // 清理测试数据
            redisTemplate.delete(testKey);
            
        } catch (Exception e) {
            redisInfo.put("status", "连接异常");
            redisInfo.put("error", e.getMessage());
            redisInfo.put("errorType", e.getClass().getSimpleName());
            redisInfo.put("connectionTest", "异常");
        }
        
        return GlobalResponse.success(redisInfo);
    }
    
    @GetMapping("/redis-test")
    @Operation(summary = "Redis功能测试", description = "测试Redis基本功能")
    public GlobalResponse<Map<String, Object>> testRedis() {
        Map<String, Object> testResult = new HashMap<>();
        
        try {
            String testKey = "test:functionality:" + System.currentTimeMillis();
            String testValue = "Hello Redis with Lettuce!";
            
            // 测试基本的set/get操作
            redisTemplate.opsForValue().set(testKey, testValue);
            String retrievedValue = (String) redisTemplate.opsForValue().get(testKey);
            
            testResult.put("set/get测试", testValue.equals(retrievedValue) ? "通过" : "失败");
            testResult.put("原始值", testValue);
            testResult.put("获取值", retrievedValue);
            
            // 测试计数器
            String counterKey = "test:counter:" + System.currentTimeMillis();
            Long counter = redisTemplate.opsForValue().increment(counterKey);
            testResult.put("计数器测试", counter == 1 ? "通过" : "失败");
            testResult.put("计数器值", counter);
            
            // 清理测试数据
            redisTemplate.delete(testKey);
            redisTemplate.delete(counterKey);
            
            testResult.put("整体状态", "所有测试通过");
            
        } catch (Exception e) {
            testResult.put("整体状态", "测试失败");
            testResult.put("错误信息", e.getMessage());
        }
        
        return GlobalResponse.success(testResult);
    }
}
