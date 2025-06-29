//package org.xiaoxingbomei.config.http;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
///**
// * 请求拦截器配置类
// *
// * 【什么是拦截器】
// * 拦截器是Spring MVC提供的机制，可以在请求处理的不同阶段进行拦截处理，
// * 类似于Servlet的Filter，但更加灵活和强大。
// *
// * 【拦截器的执行顺序】
// * 1. preHandle()：控制器方法执行前
// * 2. postHandle()：控制器方法执行后，视图渲染前
// * 3. afterCompletion()：整个请求完成后
// *
// * 【与Filter的区别】
// * 1. Filter是Servlet规范，拦截器是Spring MVC规范
// * 2. Filter在DispatcherServlet前执行，拦截器在DispatcherServlet内部执行
// * 3. 拦截器可以访问Controller的处理器信息，Filter不能
// * 4. 拦截器配置更灵活，可以精确控制拦截路径
// *
// * 【应用场景】
// * - 请求日志记录
// * - 性能监控
// * - 权限检查
// * - 登录验证
// * - 跨域处理等
// */
//@Slf4j
//@Configuration
//public class RequestInterceptorConfig implements WebMvcConfigurer {
//
//    /**
//     * 注册拦截器
//     *
//     * 【配置说明】
//     * 通过InterceptorRegistry可以注册多个拦截器，并配置拦截规则
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
//        /**
//         * 【请求日志拦截器注册】
//         *
//         * addPathPatterns("/**")：拦截所有请求路径
//         * excludePathPatterns()：排除特定路径，避免静态资源和系统路径产生大量无用日志
//         *
//         * 排除路径说明：
//         * - /swagger-ui/**：Swagger文档相关静态资源
//         * - /v3/api-docs/**：OpenAPI文档接口
//         * - /static/**：项目静态资源
//         * - /upload/**：上传文件访问路径
//         * - /actuator/**：Spring Boot监控端点
//         */
//        registry.addInterceptor(new RequestLoggingInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                    "/swagger-ui/**",
//                    "/v3/api-docs/**",
//                    "/static/**",
//                    "/upload/**",
//                    "/actuator/**"
//                );
//    }
//
//    /**
//     * 请求日志拦截器内部类
//     *
//     * 【功能职责】
//     * 1. 记录请求的详细信息（方法、路径、IP、User-Agent等）
//     * 2. 计算请求处理时间
//     * 3. 根据响应状态和耗时选择合适的日志级别
//     * 4. 提供完整的请求-响应日志链路追踪
//     */
//    private static class RequestLoggingInterceptor implements HandlerInterceptor {
//
//        /**
//         * 请求开始时间在请求属性中的键名
//         * 用于在preHandle和afterCompletion之间传递数据
//         */
//        private static final String START_TIME = "startTime";
//
//        /**
//         * 请求前置处理
//         *
//         * 【执行时机】
//         * 在Controller方法执行之前调用
//         *
//         * 【主要功能】
//         * 1. 记录请求开始时间（用于计算响应时间）
//         * 2. 提取并记录请求的关键信息
//         * 3. 进行必要的请求预处理
//         *
//         * 【返回值说明】
//         * - true：继续执行后续的拦截器和Controller
//         * - false：中断请求处理，直接返回响应
//         */
//        @Override
//        public boolean preHandle(HttpServletRequest request,
//                               HttpServletResponse response,
//                               Object handler) throws Exception {
//
//            // 记录请求开始时间，用于后续计算处理耗时
//            long startTime = System.currentTimeMillis();
//            request.setAttribute(START_TIME, startTime);
//
//            /**
//             * 【提取请求信息】
//             * 收集请求的各种关键信息，便于问题排查和监控分析
//             */
//            String method = request.getMethod();                    // HTTP方法（GET/POST等）
//            String uri = request.getRequestURI();                   // 请求路径
//            String queryString = request.getQueryString();          // 查询参数
//            String remoteAddr = getClientIpAddress(request);        // 客户端真实IP
//            String userAgent = request.getHeader("User-Agent");     // 用户代理信息
//
//            /**
//             * 【请求日志输出】
//             * 使用统一格式记录请求信息，便于日志分析和监控
//             *
//             * 格式：==> 方法 路径 查询参数 | IP: xxx | UA: xxx
//             * 示例：==> GET /api/user/123 ?name=test | IP: 192.168.1.100 | UA: Mozilla/5.0...
//             */
//            log.info("==> {} {} {} | IP: {} | UA: {}",
//                    method, uri,
//                    queryString != null ? "?" + queryString : "",
//                    remoteAddr,
//                    // 截断User-Agent避免日志过长
//                    userAgent != null ? userAgent.substring(0, Math.min(userAgent.length(), 100)) : "Unknown"
//            );
//
//            return true;
//        }
//
//        /**
//         * 请求完成后处理
//         *
//         * 【执行时机】
//         * 在整个请求处理完成后调用（包括视图渲染完成）
//         * 即使出现异常也会执行
//         *
//         * 【主要功能】
//         * 1. 计算请求处理总耗时
//         * 2. 记录响应状态和异常信息
//         * 3. 根据响应情况选择合适的日志级别
//         * 4. 提供完整的请求处理结果
//         */
//        @Override
//        public void afterCompletion(HttpServletRequest request,
//                                  HttpServletResponse response,
//                                  Object handler, Exception ex) throws Exception {
//
//            // 获取请求开始时间，计算总耗时
//            Long startTime = (Long) request.getAttribute(START_TIME);
//            if (startTime != null) {
//                long endTime = System.currentTimeMillis();
//                long duration = endTime - startTime;
//
//                String method = request.getMethod();
//                String uri = request.getRequestURI();
//                int status = response.getStatus();
//
//                /**
//                 * 【智能日志级别选择】
//                 * 根据请求处理结果自动选择合适的日志级别：
//                 *
//                 * 1. ERROR级别：发生未捕获异常
//                 * 2. WARN级别：慢请求（>2秒）或HTTP错误状态（>=400）
//                 * 3. INFO级别：正常请求
//                 *
//                 * 这样可以快速识别系统问题和性能瓶颈
//                 */
//                if (ex != null) {
//                    // 请求处理过程中发生异常
//                    log.error("<== {} {} | Status: {} | Time: {}ms | Error: {}",
//                            method, uri, status, duration, ex.getMessage());
//                } else if (duration > 2000) {
//                    // 慢请求警告（超过2秒）
//                    log.warn("<== {} {} | Status: {} | Time: {}ms [SLOW]",
//                            method, uri, status, duration);
//                } else if (status >= 400) {
//                    // HTTP错误状态码
//                    log.warn("<== {} {} | Status: {} | Time: {}ms [ERROR]",
//                            method, uri, status, duration);
//                } else {
//                    // 正常请求
//                    log.info("<== {} {} | Status: {} | Time: {}ms",
//                            method, uri, status, duration);
//                }
//            }
//        }
//
//        /**
//         * 获取客户端真实IP地址
//         *
//         * 【为什么需要这个方法】
//         * 在负载均衡、反向代理等网络架构中，request.getRemoteAddr()获取的
//         * 往往是代理服务器的IP，而不是真实客户端IP。需要通过特定的HTTP头获取真实IP。
//         *
//         * 【IP获取优先级】
//         * 1. X-Forwarded-For：最常用的代理转发头，可能包含IP链
//         * 2. X-Real-IP：Nginx等常用的真实IP头
//         * 3. X-Client-IP：某些代理使用的客户端IP头
//         * 4. RemoteAddr：直接连接时的IP地址
//         *
//         * 【安全考虑】
//         * 这些HTTP头都可能被客户端伪造，在安全要求高的场景下需要额外验证
//         */
//        private String getClientIpAddress(HttpServletRequest request) {
//            String xForwardedFor = request.getHeader("X-Forwarded-For");
//            String xRealIp = request.getHeader("X-Real-IP");
//            String xClientIp = request.getHeader("X-Client-IP");
//            String remoteAddr = request.getRemoteAddr();
//
//            /**
//             * X-Forwarded-For头格式：client_ip, proxy1_ip, proxy2_ip
//             * 第一个IP通常是真实客户端IP
//             */
//            if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
//                return xForwardedFor.split(",")[0].trim();
//            }
//
//            if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
//                return xRealIp;
//            }
//
//            if (xClientIp != null && !xClientIp.isEmpty() && !"unknown".equalsIgnoreCase(xClientIp)) {
//                return xClientIp;
//            }
//
//            // 最后使用直接连接的IP
//            return remoteAddr;
//        }
//    }
//}