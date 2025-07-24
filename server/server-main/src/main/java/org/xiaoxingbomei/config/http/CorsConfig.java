package org.xiaoxingbomei.config.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域资源共享(CORS)配置类
 * 
 * 【什么是CORS】
 * CORS (Cross-Origin Resource Sharing) 是一种安全机制，用于控制哪些外部域名可以访问当前域名的资源。
 * 
 * 【为什么需要CORS配置】
 * 浏览器的同源策略默认禁止跨域请求，当前端应用(如Vue、React)与后端API不在同一域名时，
 * 就需要配置CORS来允许跨域访问，否则会出现"blocked by CORS policy"错误。
 * 
 * 【工作原理】
 * 1. 简单请求：浏览器直接发送请求，服务器返回CORS头判断是否允许
 * 2. 复杂请求：浏览器先发送OPTIONS预检请求，确认允许后再发送实际请求
 * 
 * 【应用场景】
 * - 前后端分离项目
 * - 微服务架构中不同服务间调用
 * - 第三方应用集成
 */
@Configuration
public class CorsConfig {

    /**
     * 配置CORS过滤器
     * 
     * 【实现原理】
     * 通过CorsFilter在请求处理前添加相应的CORS响应头，告诉浏览器允许跨域访问
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        /**
         * 【允许的源域名】
         * addAllowedOriginPattern("*") - 允许所有域名访问
         * 
         * 原理：设置 Access-Control-Allow-Origin 响应头
         * 生产环境建议指定具体域名，如：
         * config.addAllowedOrigin("https://www.example.com");
         * config.addAllowedOrigin("http://localhost:3000"); // 开发环境
         */
        config.addAllowedOriginPattern("*");
        
        /**
         * 【允许的请求头】
         * addAllowedHeader("*") - 允许客户端发送任何请求头
         * 
         * 原理：设置 Access-Control-Allow-Headers 响应头
         * 常见的请求头：Content-Type, Authorization, X-Requested-With 等
         */
        config.addAllowedHeader("*");
        
        /**
         * 【允许的HTTP方法】
         * addAllowedMethod("*") - 允许所有HTTP方法
         * 
         * 原理：设置 Access-Control-Allow-Methods 响应头
         * 包括：GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH 等
         */
        config.addAllowedMethod("*");
        
        /**
         * 【允许携带凭证】
         * setAllowCredentials(true) - 允许发送Cookie、Authorization头等凭证信息
         * 
         * 原理：设置 Access-Control-Allow-Credentials: true 响应头
         * 注意：当设置为true时，Access-Control-Allow-Origin不能为*，需要指定具体域名
         * 但使用addAllowedOriginPattern("*")可以绕过这个限制
         */
        config.setAllowCredentials(true);
        
        /**
         * 【预检请求缓存时间】
         * setMaxAge(3600L) - 预检请求结果缓存1小时
         * 
         * 原理：设置 Access-Control-Max-Age 响应头
         * 作用：减少预检请求次数，提高性能
         * 在缓存时间内，浏览器不会重复发送OPTIONS预检请求
         */
        config.setMaxAge(3600L);
        
        /**
         * 【暴露的响应头】
         * addExposedHeader() - 允许客户端JavaScript访问的响应头
         * 
         * 原理：设置 Access-Control-Expose-Headers 响应头
         * 默认情况下，客户端只能访问基本响应头，自定义头需要显式暴露
         */
        config.addExposedHeader("Content-Disposition"); // 文件下载时的文件名
        config.addExposedHeader("Content-Type");        // 响应内容类型
        config.addExposedHeader("Authorization");       // 认证令牌
        
        /**
         * 【注册CORS配置】
         * 将配置应用到所有路径（/**）
         */
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
} 