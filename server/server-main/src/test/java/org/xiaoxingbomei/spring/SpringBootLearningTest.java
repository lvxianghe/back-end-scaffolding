package org.xiaoxingbomei.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.xiaoxingbomei.service.TestService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Spring Boot 学习测试
 * 学习Spring Boot的核心概念
 */
@SpringBootTest
@ActiveProfiles("test")
public class SpringBootLearningTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TestService testService;

    @Test
    void testApplicationContextLoaded() {
        // 测试Spring应用上下文是否正确加载
        assertNotNull(applicationContext);
        
        // 检查Bean是否存在
        assertTrue(applicationContext.containsBean("testService"));
        assertTrue(applicationContext.containsBean("testRepository"));
    }

    @Test
    void testDependencyInjection() {
        // 测试依赖注入是否工作
        assertNotNull(testService);
        
        // 测试Service方法
        assertDoesNotThrow(() -> {
            testService.getAllUsers();
        });
    }

    @Test
    void testServiceLayer() {
        // 测试Service层功能
        var users = testService.getAllUsers();
        assertNotNull(users);
        assertFalse(users.isEmpty());
        
        // 测试创建用户
        boolean created = testService.createUser("测试用户");
        assertTrue(created);
        
        // 验证用户是否被创建
        String foundUser = testService.findUserByName("测试用户");
        assertEquals("测试用户", foundUser);
    }

    @Test
    void testProfiles() {
        // 测试Profile配置
        String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
        assertTrue(activeProfiles.length > 0);
    }
} 