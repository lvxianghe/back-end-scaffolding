package org.xiaoxingbomei.basic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Java基础知识学习测试
 */
public class JavaBasicsTest {

    @Test
    @DisplayName("集合操作学习")
    void testCollections() {
        // List 操作
        List<String> list = new ArrayList<>();
        list.add("苹果");
        list.add("香蕉");
        list.add("橙子");
        
        assertEquals(3, list.size());
        assertTrue(list.contains("苹果"));
        
        // Set 操作
        Set<String> set = new HashSet<>(list);
        set.add("苹果"); // 重复元素不会被添加
        assertEquals(3, set.size());
        
        // Map 操作
        Map<String, Integer> fruitPrices = new HashMap<>();
        fruitPrices.put("苹果", 5);
        fruitPrices.put("香蕉", 3);
        fruitPrices.put("橙子", 4);
        
        assertEquals(Integer.valueOf(5), fruitPrices.get("苹果"));
    }

    @Test
    @DisplayName("Stream API学习")
    void testStreamAPI() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // 过滤偶数
        List<Integer> evenNumbers = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        assertEquals(5, evenNumbers.size());
        
        // 映射操作
        List<Integer> doubled = numbers.stream()
                .map(n -> n * 2)
                .collect(Collectors.toList());
        assertEquals(Integer.valueOf(20), doubled.get(9));
        
        // 求和
        int sum = numbers.stream()
                .mapToInt(Integer::intValue)
                .sum();
        assertEquals(55, sum);
    }

    @Test
    @DisplayName("字符串操作学习")
    void testStringOperations() {
        String text = "Hello World Java Spring Boot";
        
        // 基本操作
        assertTrue(text.contains("Java"));
        assertEquals("HELLO WORLD JAVA SPRING BOOT", text.toUpperCase());
        
        // 分割字符串
        String[] words = text.split(" ");
        assertEquals(5, words.length);
        
        // 字符串拼接
        String joined = String.join("-", words);
        assertEquals("Hello-World-Java-Spring-Boot", joined);
        
        // StringBuilder
        StringBuilder sb = new StringBuilder();
        sb.append("学习").append("Java").append("很有趣");
        assertEquals("学习Java很有趣", sb.toString());
    }

    @Test
    @DisplayName("异常处理学习")
    void testExceptionHandling() {
        // 测试异常抛出
        assertThrows(IllegalArgumentException.class, () -> {
            validateAge(-1);
        });
        
        // 测试正常情况
        assertDoesNotThrow(() -> {
            validateAge(25);
        });
    }
    
    private void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("年龄不能为负数");
        }
    }
} 