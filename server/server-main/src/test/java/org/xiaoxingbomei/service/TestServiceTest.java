package org.xiaoxingbomei.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Service 测试示例
 * 学习Mockito和单元测试
 */
@ExtendWith(MockitoExtension.class)
public class TestServiceTest {

    @Mock
    private TestRepository testRepository;

    @InjectMocks
    private TestService testService;

    @BeforeEach
    void setUp() {
        // 测试前的准备工作
    }

    @Test
    void testGetAllUsers() {
        // Given - 准备测试数据
        List<String> mockUsers = List.of("张三", "李四", "王五");
        when(testRepository.findAllUsers()).thenReturn(mockUsers);

        // When - 执行测试方法
        List<String> result = testService.getAllUsers();

        // Then - 验证结果
        assertEquals(3, result.size());
        assertTrue(result.contains("张三"));
        verify(testRepository, times(1)).findAllUsers();
    }

    @Test
    void testCreateUser() {
        // Given
        String userName = "新用户";
        when(testRepository.save(userName)).thenReturn(true);

        // When
        boolean result = testService.createUser(userName);

        // Then
        assertTrue(result);
        verify(testRepository).save(userName);
    }

    @Test
    void testCreateUserWithEmptyName() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            testService.createUser("");
        });
    }
} 