package org.xiaoxingbomei.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 统一多数据源配置类（简化版）
 * 只创建数据源，暂不涉及 MyBatis 和事务管理器
 */
@Configuration
public class UnifiedDataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(UnifiedDataSourceConfig.class);

    @Autowired
    private MultiDataSourceProperties multiDataSourceProperties;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void initDataSources() {
        if (multiDataSourceProperties.getDatasources() == null || 
            multiDataSourceProperties.getDatasources().isEmpty()) {
            logger.warn("未配置任何数据源，跳过数据源初始化");
            return;
        }

        ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableContext.getBeanFactory();

        // 遍历配置的数据源进行初始化
        multiDataSourceProperties.getDatasources().forEach((name, config) -> {
            try {
                logger.info("开始初始化数据源: {}", name);
                
                // 1. 创建数据源
                currentDataSourceName = name; // 设置当前数据源名称，用于日志记录
                DataSource dataSource = createDataSource(config);
                String dataSourceBeanName = name + "DataSource";
                beanFactory.registerSingleton(dataSourceBeanName, dataSource);
                
                logger.info("数据源 {} 初始化完成", name);
                
            } catch (Exception e) {
                logger.error("初始化数据源 {} 失败", name, e);
                throw new RuntimeException("数据源初始化失败: " + name, e);
            }
        });
    }

    /**
     * 创建数据源
     */
    private DataSource createDataSource(MultiDataSourceProperties.DataSourceConfig config) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        
        // 基本连接参数
        dataSource.setUrl(config.getUrl());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());
        dataSource.setDriverClassName(config.getDriverClassName());
        
        // 连接池配置（支持自定义，有合理默认值）
        dataSource.setInitialSize(config.getInitialSize() != null ? config.getInitialSize() : 5);
        dataSource.setMaxActive(config.getMaxActive() != null ? config.getMaxActive() : 20);
        dataSource.setMinIdle(config.getMinIdle() != null ? config.getMinIdle() : 5);
        dataSource.setMaxWait(config.getMaxWait() != null ? config.getMaxWait() : 60000L);
        
        // 固定的优化配置
        dataSource.setTestOnBorrow(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        
        // 开启 Druid 监控功能
        try {
            dataSource.setFilters("stat,wall");
        } catch (SQLException e) {
            logger.warn("设置 Druid 过滤器失败", e);
        }
        
        // 根据数据库类型设置合适的validationQuery
        String validationQuery = getValidationQueryForDatabase(config);
        dataSource.setValidationQuery(validationQuery);
        
        logger.info("数据源 {} 使用validationQuery: {}", getCurrentDataSourceName(), validationQuery);
        
        // 初始化数据源
        dataSource.init();
        
        return dataSource;
    }

    /**
     * 根据数据库类型获取合适的validationQuery
     */
    private String getValidationQueryForDatabase(MultiDataSourceProperties.DataSourceConfig config) {
        String url = config.getUrl().toLowerCase();
        String driverClassName = config.getDriverClassName().toLowerCase();
        
        // 根据URL或驱动类判断数据库类型
        if (url.contains("jdbc:db2") || driverClassName.contains("db2")) {
            return "SELECT 1 FROM SYSIBM.SYSDUMMY1";
        } else if (url.contains("jdbc:oracle") || driverClassName.contains("oracle")) {
            return "SELECT 1 FROM DUAL";
        } else if (url.contains("jdbc:postgresql") || driverClassName.contains("postgresql")) {
            return "SELECT 1";
        } else if (url.contains("jdbc:mysql") || driverClassName.contains("mysql")) {
            return "SELECT 1";
        } else if (url.contains("jdbc:sqlserver") || driverClassName.contains("sqlserver")) {
            return "SELECT 1";
        } else if (url.contains("jdbc:h2") || driverClassName.contains("h2")) {
            return "SELECT 1";
        } else {
            // 默认使用SELECT 1
            return "SELECT 1";
        }
    }

    // 用于日志记录的当前数据源名称
    private String currentDataSourceName;
    
    private String getCurrentDataSourceName() {
        return currentDataSourceName != null ? currentDataSourceName : "unknown";
    }
} 