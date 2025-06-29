package org.xiaoxingbomei.config.http;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 * 
 * 【作用说明】
 * 通过实现WebMvcConfigurer接口来自定义Spring MVC的行为，包括：
 * - 静态资源处理策略
 * - 内容协商机制
 * - 视图解析器配置
 * - 拦截器注册等
 * 
 * 【为什么需要这个配置】
 * 1. 静态资源：Spring Boot默认静态资源路径有限，需要自定义路径映射
 * 2. 缓存控制：合理的缓存策略可以提高性能，减少服务器压力
 * 3. 内容协商：确定客户端和服务器之间数据交换的格式
 */
@Configuration
public class WebConfig implements WebMvcConfigurer
{

    /**
     * 配置静态资源处理器
     * 
     * 【什么是静态资源】
     * 静态资源是指不需要服务器处理的文件，如HTML、CSS、JS、图片、文档等
     * 
     * 【工作原理】
     * Spring MVC通过ResourceHandler将特定URL模式映射到文件系统或classpath中的资源位置
     * 当请求匹配到对应模式时，直接返回静态文件，不经过Controller处理
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        
        /**
         * 【Swagger UI 静态资源配置】
         * 
         * 目的：让Swagger UI的静态文件(HTML、CSS、JS)能够被正确访问
         * 
         * 映射关系：
         * 请求路径：/swagger-ui/** (如：/swagger-ui/index.html)
         * 实际位置：classpath:/META-INF/resources/webjars/swagger-ui/
         * 
         * 缓存策略：3600秒(1小时)，减少重复请求
         */
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/")
                .setCachePeriod(3600);
        
        /**
         * 【自定义静态资源配置】
         * 
         * 目的：为项目自定义的静态文件(如前端页面、样式等)提供访问路径
         * 
         * 映射关系：
         * 请求路径：/static/** (如：/static/css/style.css)
         * 实际位置：classpath:/static/ (即resources/static目录)
         * 
         * 使用场景：存放项目自定义的CSS、JS、图片等静态资源
         */
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
                
        /**
         * 【文件上传访问配置】
         * 
         * 目的：为用户上传的文件提供HTTP访问路径
         * 
         * 映射关系：
         * 请求路径：/upload/** (如：/upload/images/avatar.jpg)
         * 实际位置：file:upload/ (即项目根目录下的upload文件夹)
         * 
         * 注意：
         * - file: 前缀表示文件系统路径，不是classpath
         * - 生产环境建议使用绝对路径或专门的文件服务器
         * - 需要确保upload目录有适当的读写权限
         */
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:upload/")
                .setCachePeriod(3600);
    }

    /**
     * 配置内容协商策略
     * 
     * 【什么是内容协商】
     * 内容协商是HTTP协议的一个特性，用于在客户端和服务器之间确定最适合的内容格式。
     * 客户端通过Accept头告诉服务器自己支持的媒体类型，服务器根据这些信息返回合适的内容。
     * 
     * 【协商方式】
     * 1. 基于URL参数：如 /api/user?format=json
     * 2. 基于文件扩展名：如 /api/user.json
     * 3. 基于Accept请求头：如 Accept: application/json
     * 
     * 【配置策略】
     * 优先使用Accept头，这是最标准的HTTP做法
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                /**
                 * favorParameter(false) - 不使用URL参数进行内容协商
                 * 
                 * 原理：禁用 ?format=json 这种方式
                 * 好处：URL更简洁，符合RESTful风格
                 */
                .favorParameter(false)
                
                /**
                 * favorPathExtension(false) - 不使用路径扩展名进行内容协商
                 * 
                 * 原理：禁用 /api/user.json 这种方式
                 * 好处：避免路径混乱，防止安全问题
                 */
                .favorPathExtension(false)
                
                /**
                 * ignoreAcceptHeader(false) - 不忽略Accept请求头
                 * 
                 * 原理：使用标准的HTTP Accept头进行内容协商
                 * 例如：Accept: application/json, text/html;q=0.9
                 */
                .ignoreAcceptHeader(false)
                
                /**
                 * defaultContentType() - 设置默认内容类型
                 * 
                 * 原理：当无法确定客户端需要什么格式时，默认返回JSON
                 * 适用场景：现代Web应用多数使用JSON格式通信
                 */
                .defaultContentType(MediaType.APPLICATION_JSON);
    }
} 