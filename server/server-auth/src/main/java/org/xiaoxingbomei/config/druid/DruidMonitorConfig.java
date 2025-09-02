package org.xiaoxingbomei.config.druid;

import com.alibaba.druid.support.jakarta.StatViewServlet;
import com.alibaba.druid.support.jakarta.WebStatFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid监控配置 - 手动配置
 *
 * @author xiaoxingbomei
 * @date 2024-01-01
 */
@Configuration
@Slf4j
public class DruidMonitorConfig
{

    /**
     * 配置Druid监控页面
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidStatViewServlet()
    {
        ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

        // 监控页面登录账号密码
        registrationBean.addInitParameter("loginUsername", "admin");
        registrationBean.addInitParameter("loginPassword", "admin");

        // IP白名单 (没有配置或者为空，则允许所有访问)
        registrationBean.addInitParameter("allow", "");

        // IP黑名单 (存在共同时，deny优先于allow)
        registrationBean.addInitParameter("deny", "");

        // 禁用HTML页面上的"Reset All"功能
        registrationBean.addInitParameter("resetEnable", "false");

        log.info("🔥 Druid监控页面配置完成: http://localhost:10001/druid/");
        return registrationBean;
    }

    /**
     * 配置Web应用统计过滤器
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> druidWebStatFilter() {
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>(new WebStatFilter());

        // 添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");

        // 排除一些不必要的url
        filterRegistrationBean.addInitParameter("exclusions",
                "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,/swagger-ui/*,/v3/api-docs/*");

        return filterRegistrationBean;
    }
}