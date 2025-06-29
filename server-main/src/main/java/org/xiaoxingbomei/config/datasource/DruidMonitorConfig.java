package org.xiaoxingbomei.config.datasource;

import com.alibaba.druid.support.jakarta.StatViewServlet;
import com.alibaba.druid.support.jakarta.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid监控页面配置
 */
@Configuration
public class DruidMonitorConfig
{

    /**
     * 配置Druid监控页面
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidStatViewServlet()
    {
        ServletRegistrationBean<StatViewServlet> registrationBean = 
            new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        
        // 登录用户名和密码
        registrationBean.addInitParameter("loginUsername", "admin");
        registrationBean.addInitParameter("loginPassword", "admin");
        
        // IP白名单（为空或者为null时，表示允许所有访问）
        registrationBean.addInitParameter("allow", "");
        
        // IP黑名单（存在共同时，deny优先于allow）
        // registrationBean.addInitParameter("deny", "192.168.1.100");
        
        // 禁用HTML页面上的"Reset All"功能
        registrationBean.addInitParameter("resetEnable", "false");
        
        return registrationBean;
    }

    /**
     * 配置Druid监控过滤器
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> druidWebStatFilter()
    {
        FilterRegistrationBean<WebStatFilter> registrationBean = 
            new FilterRegistrationBean<>(new WebStatFilter());
        
        // 拦截所有请求
        registrationBean.addUrlPatterns("/*");
        
        // 排除一些不必要的url
        registrationBean.addInitParameter("exclusions", 
            "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        
        return registrationBean;
    }
} 