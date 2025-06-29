package org.xiaoxingbomei.config.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HTTP安全配置类
 * 
 * 【什么是HTTP安全头】
 * HTTP安全头是服务器在响应中添加的特殊HTTP头部，用于指示浏览器如何处理页面内容，
 * 从而防止各种Web安全攻击，如XSS、点击劫持、内容嗅探等。
 * 
 * 【为什么需要HTTP安全头】
 * 1. 浏览器安全：现代浏览器会根据这些头部信息执行相应的安全策略
 * 2. 合规要求：很多安全扫描工具和合规标准都要求设置这些安全头
 * 3. 防御深度：多层安全防护的重要组成部分
 * 
 * 【实现方式】
 * 通过OncePerRequestFilter确保每个HTTP响应都包含这些安全头部
 */
@Configuration
public class HttpSecurityConfig {

    /**
     * HTTP安全头过滤器
     * 
     * 【工作原理】
     * OncePerRequestFilter确保在一次请求中只执行一次，避免重复添加安全头
     * 在响应返回给客户端之前，统一添加各种安全相关的HTTP头部
     */
    @Bean
    public OncePerRequestFilter httpSecurityHeadersFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, 
                                          HttpServletResponse response, 
                                          FilterChain filterChain) throws ServletException, IOException {
                
                // 添加安全响应头
                addSecurityHeaders(response);
                
                filterChain.doFilter(request, response);
            }
        };
    }

    /**
     * 添加安全响应头
     * 
     * 【核心安全头说明】
     * 每个安全头都针对特定的Web安全威胁，组合使用可以构建完整的安全防护体系
     */
    private void addSecurityHeaders(HttpServletResponse response) {
        
        /**
         * 【防点击劫持攻击】X-Frame-Options
         * 
         * 攻击原理：恶意网站通过iframe嵌入你的页面，诱导用户在不知情的情况下点击
         * 防护原理：告诉浏览器这个页面不能被嵌入到frame/iframe中
         * 
         * 可选值：
         * - DENY: 完全禁止被嵌入
         * - SAMEORIGIN: 只允许同源页面嵌入  
         * - ALLOW-FROM uri: 只允许指定页面嵌入
         */
        response.setHeader("X-Frame-Options", "DENY");
        
        /**
         * 【防MIME类型嗅探】X-Content-Type-Options
         * 
         * 攻击原理：浏览器可能忽略服务器声明的Content-Type，自动"嗅探"文件类型
         * 这可能导致恶意文件被当作可执行脚本处理
         * 
         * 防护原理：nosniff告诉浏览器严格按照Content-Type处理内容，不进行类型嗅探
         * 
         * 使用场景：特别重要用于文件上传功能，防止上传的恶意文件被执行
         */
        response.setHeader("X-Content-Type-Options", "nosniff");
        
        /**
         * 【XSS保护】X-XSS-Protection
         * 
         * 攻击原理：跨站脚本攻击，恶意脚本被注入到网页中执行
         * 防护原理：启用浏览器内置的XSS过滤器
         * 
         * 参数说明：
         * - 1: 启用XSS过滤
         * - mode=block: 检测到XSS攻击时阻止页面加载，而不是清理内容
         * 
         * 注意：现代应用更推荐使用CSP (Content Security Policy)
         */
        response.setHeader("X-XSS-Protection", "1; mode=block");
        
        /**
         * 【引用策略】Referrer-Policy
         * 
         * 作用：控制浏览器在发送请求时是否包含Referer头，以及包含多少信息
         * 隐私保护：防止敏感URL信息泄露给第三方网站
         * 
         * strict-origin-when-cross-origin 含义：
         * - 同源请求：发送完整的referer
         * - 跨源HTTPS→HTTPS：只发送origin
         * - 跨源HTTPS→HTTP：不发送referer（安全降级）
         */
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        
        /**
         * 【缓存控制】Cache-Control（敏感页面）
         * 
         * 作用：防止敏感信息被缓存在浏览器、代理服务器等中间环节
         * 
         * 指令说明：
         * - no-cache: 每次使用前必须验证
         * - no-store: 禁止存储任何缓存
         * - must-revalidate: 强制重新验证
         * 
         * 适用场景：登录页面、用户信息页面、支付页面等
         */
        if (isSensitivePage(response)) {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");         // HTTP/1.0兼容
            response.setHeader("Expires", "0");               // 立即过期
        }
        
        /**
         * 【隐藏服务器信息】Server
         * 
         * 安全原理：默认情况下，服务器会在响应中暴露详细的服务器信息
         * 如：Server: Apache/2.4.41 (Unix) OpenSSL/1.1.1d
         * 
         * 风险：攻击者可利用这些信息查找特定版本的漏洞进行针对性攻击
         * 防护：隐藏或伪造服务器信息，减少攻击面
         */
        response.setHeader("Server", "Unknown");
    }

    /**
     * 判断是否为敏感页面
     * 
     * 【判断逻辑】
     * 根据请求路径、用户状态等因素判断当前页面是否包含敏感信息
     * 
     * 【扩展建议】
     * 可以通过以下方式扩展：
     * 1. 检查URL路径（如：/admin/**, /user/profile等）
     * 2. 检查用户登录状态
     * 3. 检查请求参数
     * 4. 检查响应内容类型
     */
    private boolean isSensitivePage(HttpServletResponse response) {
        // 示例：这里可以根据实际需求判断
        // String requestURI = ((HttpServletRequest) request).getRequestURI();
        // return requestURI.startsWith("/admin/") || requestURI.startsWith("/user/");
        
        // 暂时返回false，可根据实际需求调整
        return false;
    }
} 