# 测试学习目录

这个目录包含了各种学习用的测试代码，帮助你掌握Java和Spring Boot开发。

## 📁 目录结构

```
src/test/java/org/xiaoxingbomei/
├── basic/                    # Java基础知识
│   └── JavaBasicsTest.java   # 集合、Stream、字符串等基础操作
├── algorithm/                # 算法学习
│   └── AlgorithmLearningTest.java  # 排序、查找、数学算法
├── controller/               # Web层测试
│   └── TestControllerTest.java     # Spring MVC测试
├── service/                  # 业务层测试
│   └── TestServiceTest.java        # Service层单元测试
└── spring/                   # Spring Boot学习
    └── SpringBootLearningTest.java # Spring Boot核心概念
```

## 🚀 如何运行测试

### 运行所有测试
```bash
mvn test
```

### 运行特定测试类
```bash
mvn test -Dtest=JavaBasicsTest
mvn test -Dtest=AlgorithmLearningTest
mvn test -Dtest=TestControllerTest
```

### 运行特定测试方法
```bash
mvn test -Dtest=JavaBasicsTest#testCollections
```

## 📚 学习内容

### 1. Java基础 (`JavaBasicsTest`)
- **集合操作**: List, Set, Map的使用
- **Stream API**: 函数式编程，过滤、映射、聚合操作
- **字符串处理**: 分割、拼接、StringBuilder
- **异常处理**: try-catch, 自定义异常

### 2. 算法学习 (`AlgorithmLearningTest`)
- **排序算法**: 冒泡排序、选择排序
- **查找算法**: 二分查找
- **字符串算法**: 回文检查、字符串反转、字符计数
- **数学算法**: 斐波那契、阶乘、最大公约数

### 3. Spring Boot Web (`TestControllerTest`)
- **MockMvc测试**: 模拟HTTP请求
- **JSON响应测试**: 验证API返回的JSON数据
- **参数测试**: 测试请求参数处理

### 4. Service层测试 (`TestServiceTest`)
- **Mockito使用**: Mock对象、验证调用
- **单元测试**: Given-When-Then模式
- **异常测试**: 验证异常抛出

### 5. Spring Boot核心 (`SpringBootLearningTest`)
- **依赖注入**: @Autowired的使用
- **应用上下文**: ApplicationContext
- **Profile配置**: 不同环境配置

## 💡 学习建议

1. **从基础开始**: 先运行`JavaBasicsTest`，理解Java基础概念
2. **算法练习**: 通过`AlgorithmLearningTest`提升编程思维
3. **Spring Boot**: 学习`SpringBootLearningTest`了解框架核心
4. **Web开发**: 通过`TestControllerTest`学习Web API测试
5. **测试驱动**: 学习`TestServiceTest`掌握单元测试技巧

## 🔧 扩展练习

你可以在这些测试类中添加更多的测试方法来练习：

- 添加更多排序算法（快速排序、归并排序）
- 实现更多数据结构（栈、队列、链表）
- 添加更多Spring Boot特性测试
- 练习数据库操作测试

## 📖 相关资源

- [JUnit 5 官方文档](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito 官方文档](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot 测试指南](https://spring.io/guides/gs/testing-web/)

祝你学习愉快！🎉 