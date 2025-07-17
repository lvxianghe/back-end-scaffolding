package org.xiaoxingbomei.config.satoken;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [Sa-Token 权限认证] Gateway配置类
 * 适配Spring Cloud Gateway的响应式环境
 * 
 * @author xiaoxingbomei
 * @date 2024-01-01
 */
@Configuration
public class SaTokenConfigure
{

    /**
     * 注册 Sa-Token全局过滤器
     */
    @Bean
    public SaReactorFilter getSaReactorFilter()
    {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")    /* 拦截全部path */
                
                // 开放地址 - 不需要登录的接口
                .addExclude("/favicon.ico")
                .addExclude("/actuator/**")      // 健康检查
                .addExclude("/auth/login")       // 登录接口
                .addExclude("/login")           // 主服务登录入口
                .addExclude("/auth/isLogin")    // 登录状态检查
                .addExclude("/health/**")       // 健康检查
                
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 登录校验
                    SaRouter.match("/**")
                        .notMatch("/auth/login", "/login", "/auth/isLogin", "/health/**", "/actuator/**")
                        .check(r -> StpUtil.checkLogin());

                    // 权限认证 - 不同模块校验不同权限
                    SaRouter.match("/auth/users/**", r -> StpUtil.checkPermission("user:manage"));
                    SaRouter.match("/token/kickout/**", r -> StpUtil.checkPermission("token:manage"));
                    SaRouter.match("/token/blacklist", r -> StpUtil.checkPermission("token:manage"));
                    SaRouter.match("/api/system/update", r -> StpUtil.checkPermission("system:manage"));
                    
                    // AI服务权限控制
                    SaRouter.match("/ai/**", "/chat/**", r -> StpUtil.checkPermission("ai:use"));
                })
                
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> {
                    // 根据不同异常返回不同信息
                    return SaResult.error("Gateway认证失败: " + e.getMessage()).setCode(401);
                })
                ;
    }
}
