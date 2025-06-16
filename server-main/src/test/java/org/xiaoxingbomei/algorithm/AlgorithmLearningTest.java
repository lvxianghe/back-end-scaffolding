package org.xiaoxingbomei.algorithm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 算法学习测试
 * 常见算法和数据结构练习
 */
public class AlgorithmLearningTest {

    @Test
    @DisplayName("排序算法学习")
    void testSortingAlgorithms() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        
        // 冒泡排序
        int[] bubbleSorted = bubbleSort(arr.clone());
        assertArrayEquals(new int[]{11, 12, 22, 25, 34, 64, 90}, bubbleSorted);
        
        // 选择排序
        int[] selectionSorted = selectionSort(arr.clone());
        assertArrayEquals(new int[]{11, 12, 22, 25, 34, 64, 90}, selectionSorted);
    }

    @Test
    @DisplayName("查找算法学习")
    void testSearchAlgorithms() {
        int[] sortedArr = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19};
        
        // 二分查找
        int index = binarySearch(sortedArr, 7);
        assertEquals(3, index);
        
        int notFound = binarySearch(sortedArr, 8);
        assertEquals(-1, notFound);
    }

    @Test
    @DisplayName("字符串算法学习")
    void testStringAlgorithms() {
        // 回文检查
        assertTrue(isPalindrome("racecar"));
        assertFalse(isPalindrome("hello"));
        
        // 字符串反转
        assertEquals("olleh", reverseString("hello"));
        
        // 字符计数
        Map<Character, Integer> charCount = countCharacters("hello world");
        assertEquals(Integer.valueOf(3), charCount.get('l'));
        assertEquals(Integer.valueOf(2), charCount.get('o'));
    }

    @Test
    @DisplayName("数学算法学习")
    void testMathAlgorithms() {
        // 斐波那契数列
        assertEquals(55, fibonacci(10));
        assertEquals(1, fibonacci(1));
        assertEquals(1, fibonacci(2));
        
        // 阶乘
        assertEquals(120, factorial(5));
        assertEquals(1, factorial(0));
        
        // 最大公约数
        assertEquals(6, gcd(18, 24));
        assertEquals(1, gcd(17, 19));
    }

    // 冒泡排序实现
    private int[] bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        return arr;
    }

    // 选择排序实现
    private int[] selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
        return arr;
    }

    // 二分查找实现
    private int binarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) return mid;
            if (arr[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    // 回文检查
    private boolean isPalindrome(String str) {
        int left = 0, right = str.length() - 1;
        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    // 字符串反转
    private String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    // 字符计数
    private Map<Character, Integer> countCharacters(String str) {
        Map<Character, Integer> count = new HashMap<>();
        for (char c : str.toCharArray()) {
            if (c != ' ') {
                count.put(c, count.getOrDefault(c, 0) + 1);
            }
        }
        return count;
    }

    // 斐波那契数列
    private int fibonacci(int n) {
        if (n <= 2) return 1;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    // 阶乘
    private int factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    // 最大公约数
    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }
} 