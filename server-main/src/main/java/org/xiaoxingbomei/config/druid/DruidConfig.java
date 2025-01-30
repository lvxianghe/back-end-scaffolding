package org.xiaoxingbomei.config.druid;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DruidConfig
{
    // 通过 Apollo 配置中心加载 Druid 相关配置
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.druid.initial-size}")
    private int initialSize;

    @Value("${spring.datasource.druid.min-idle}")
    private int minIdle;

    @Value("${spring.datasource.druid.max-active}")
    private int maxActive;

    @Value("${spring.datasource.druid.max-wait}")
    private long maxWait;
    // =================================================================

    @Bean
    public DataSource dataSource() throws SQLException
    {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        // 其他常用配置项可以在此继续配置，例如SQL监控、连接池过滤器等
        return dataSource;
    }

//    /**
//     * 配置 Druid 监控页面 Servlet
//     * 访问路径为 /druid/*，登录使用账号和密码
//     */
//    @Bean
//    public ServletRegistrationBean<DruidStatViewServlet> druidStatViewServlet() {
//        ServletRegistrationBean<DruidStatViewServlet> servletRegistrationBean =
//                new ServletRegistrationBean<>(new DruidStatViewServlet(), "/druid/*");
//
//        // 配置登录账号和密码
//        servletRegistrationBean.addInitParameter("loginUsername", "admin");
//        servletRegistrationBean.addInitParameter("loginPassword", "admin");
//
//        // 配置允许查看的 IP，防止非授权的访问
//        servletRegistrationBean.addInitParameter("allow", "127.0.0.1,0:0:0:0:0:0:0:1");
//
//        return servletRegistrationBean;
//    }
//
//    /**
//     * 配置 Druid 监控的 Filter
//     * 用于对请求进行统计
//     */
//    @Bean
//    public FilterRegistrationBean<StatFilter> statFilter() {
//        FilterRegistrationBean<StatFilter> filterRegistrationBean =
//                new FilterRegistrationBean<>(new StatFilter());
//
//        // 配置不需要监控的路径
//        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.css,/druid/*");
//
//        return filterRegistrationBean;
//    }

}
