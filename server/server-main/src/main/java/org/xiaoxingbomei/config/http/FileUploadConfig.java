package org.xiaoxingbomei.config.http;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import jakarta.servlet.MultipartConfigElement;

/**
 * 文件上传配置类
 * 
 * 【什么是文件上传】
 * HTTP协议通过multipart/form-data编码方式支持文件上传。
 * Spring Boot默认集成了文件上传功能，但默认配置可能不满足实际需求。
 * 
 * 【为什么需要配置文件上传】
 * 1. 大小限制：防止大文件攻击，控制服务器资源消耗
 * 2. 性能优化：合理配置缓冲区和临时目录，提高上传效率
 * 3. 安全防护：限制上传大小，防止磁盘空间被耗尽
 * 4. 用户体验：根据业务需求调整上传限制
 * 
 * 【配置要素】
 * - maxFileSize：单个文件大小限制
 * - maxRequestSize：整个请求大小限制
 * - fileSizeThreshold：内存缓冲阈值
 * - location：临时文件存储位置
 */
@Configuration
public class FileUploadConfig {

    /**
     * 文件上传配置元素
     * 
     * 【配置原理】
     * MultipartConfigElement是Servlet 3.0规范中定义的文件上传配置，
     * Spring Boot通过这个配置来控制文件上传的各项参数。
     */
    @Bean
    public MultipartConfigElement multipartConfigElement()
    {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        
        /**
         * 【单个文件大小限制】maxFileSize
         * 
         * 作用：限制单个上传文件的最大大小
         * 默认值：1MB
         * 配置值：10MB
         * 
         * 考虑因素：
         * - 业务需求：根据实际业务场景调整（图片、文档、视频等）
         * - 网络环境：考虑用户网络上传速度
         * - 存储容量：服务器磁盘空间限制
         * - 安全风险：防止恶意大文件攻击
         */
        factory.setMaxFileSize(DataSize.ofMegabytes(10));
        
        /**
         * 【请求总大小限制】maxRequestSize
         * 
         * 作用：限制整个HTTP请求的最大大小（包括所有文件和表单数据）
         * 默认值：10MB
         * 配置值：50MB
         * 
         * 使用场景：
         * - 多文件上传：支持同时上传多个文件
         * - 表单数据：文件上传通常伴随其他表单字段
         * - 批量操作：支持批量文件处理功能
         * 
         * 注意：maxRequestSize应该 >= maxFileSize * 预期文件数量
         */
        factory.setMaxRequestSize(DataSize.ofMegabytes(50));
        
        /**
         * 【临时文件存储位置】location
         * 
         * 作用：指定临时文件的存储目录
         * 配置值：System.getProperty("java.io.tmpdir") - 系统临时目录
         * 
         * 工作原理：
         * 当上传文件大小超过内存阈值时，文件内容会暂存到此目录
         * 请求处理完成后，临时文件会被自动删除
         * 
         * 系统临时目录说明：
         * - Windows: C:\\Users\\用户名\\AppData\\Local\\Temp
         * - Linux/Mac: /tmp
         * - 优点：系统自动管理，权限无问题
         * - 缺点：可能被系统清理程序删除
         * 
         * 配置建议：
         * - 开发环境：使用系统临时目录
         * - 生产环境：可指定专门目录，如 /var/tmp/uploads
         * - 权限设置：确保应用有读写权限
         * - 磁盘空间：选择空间充足的磁盘分区
         */
        factory.setLocation(System.getProperty("java.io.tmpdir"));
        
        return factory.createMultipartConfig();
    }

    /**
     * 文件上传解析器
     * 
     * 【什么是MultipartResolver】
     * MultipartResolver是Spring MVC用于解析multipart请求的接口，
     * 负责将HTTP请求中的文件数据解析为MultipartFile对象。
     * 
     * 【StandardServletMultipartResolver说明】
     * - 基于Servlet 3.0的标准实现
     * - 使用Servlet容器的原生multipart支持
     * - 性能更好，配置更简单
     * - Spring Boot 2.x+的默认选择
     * 
     * 【懒加载配置】
     * setResolveLazily(true) - 启用懒加载解析
     * 
     * 作用：只有在实际访问MultipartFile时才进行解析
     * 好处：
     * 1. 性能优化：避免不必要的文件解析
     * 2. 内存节省：延迟内存分配
     * 3. 异常处理：可以在Controller中更好地处理解析异常
     * 
     * 适用场景：
     * - 条件文件处理：不是所有请求都需要处理文件
     * - 大文件上传：避免过早占用大量内存
     * - 异常控制：需要在业务层处理文件解析异常
     * 
     * 【使用示例】
     * Controller中可以通过以下方式接收上传文件：
     * 
     * @PostMapping("/upload")
     * public String upload(@RequestParam("file") MultipartFile file) {
     *     if (!file.isEmpty()) {
     *         // 处理上传的文件
     *         String filename = file.getOriginalFilename();
     *         byte[] bytes = file.getBytes();
     *         // ... 保存文件逻辑
     *     }
     *     return "success";
     * }
     */
    @Bean
    public MultipartResolver multipartResolver() {
        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
        resolver.setResolveLazily(true); // 懒加载，避免立即解析
        return resolver;
    }
} 