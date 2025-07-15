package org.xiaoxingbomei.config.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 多数据源工厂管理器
 */
@Configuration
@EnableConfigurationProperties(MultipleDataSourceProperties.class)
@Slf4j
public class MultiDataSourceFactory
{
    @Autowired
    private MultipleDataSourceProperties properties;
    
    @Autowired
    private ApplicationContext applicationContext;

    private final Map<String, DataSource> dataSourceMap = new HashMap<>();
    private final Map<String, EntityManagerFactory> entityManagerFactoryMap = new HashMap<>();
    private final Map<String, PlatformTransactionManager> transactionManagerMap = new HashMap<>();
    
    private String primaryDataSourceName;

    @PostConstruct
    public void initializeDataSources()
    {
        log.info("开始初始化多数据源...");
        
        // 调试信息：打印配置
        log.info("配置的数据源数量: {}", properties.getDataSources().size());
        properties.getDataSources().forEach((name, config) -> {
            log.info("数据源名称: {}, 是否为主数据源: {}", name, config.isPrimary());
        });

        if (properties.getDataSources().isEmpty()) {
            throw new IllegalStateException("No datasource configured in application.yml");
        }

        // 查找主数据源
        primaryDataSourceName = findPrimaryDataSourceName();
        log.info("找到主数据源: {}", primaryDataSourceName);

        // 初始化所有数据源
        properties.getDataSources().forEach((name, config) -> {
            log.info("正在初始化数据源: {}", name);
            
            // 验证配置
            validateDataSourceConfig(name, config);

            // 创建数据源
            DataSource dataSource = createDataSource(config);
            dataSourceMap.put(name, dataSource);

            // 创建EntityManagerFactory
            EntityManagerFactory emf = createEntityManagerFactory(name, dataSource, config);
            entityManagerFactoryMap.put(name, emf);

            // 创建事务管理器
            PlatformTransactionManager txManager = createTransactionManager(name, emf);
            transactionManagerMap.put(name, txManager);

            log.info("数据源 {} 初始化完成", name);
        });

        log.info("所有数据源初始化完成，共 {} 个数据源，主数据源: {}", dataSourceMap.size(), primaryDataSourceName);
    }

    /**
     * 查找主数据源名称
     */
    private String findPrimaryDataSourceName() {
        // 方法1：查找标记为primary的数据源
        String primaryName = properties.getDataSources().entrySet().stream()
                .filter(entry -> entry.getValue().isPrimary())
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        
        if (primaryName != null) {
            return primaryName;
        }
        
        // 方法2：如果没有标记primary，使用第一个数据源作为主数据源
        if (!properties.getDataSources().isEmpty()) {
            String firstDataSourceName = properties.getDataSources().keySet().iterator().next();
            log.warn("没有找到标记为primary的数据源，将使用第一个数据源 '{}' 作为主数据源", firstDataSourceName);
            return firstDataSourceName;
        }
        
        throw new IllegalStateException("No primary datasource found and no datasource configured");
    }

    /**
     * 验证数据源配置
     */
    private void validateDataSourceConfig(String name, MultipleDataSourceProperties.DataSourceConfig config) {
        if (!StringUtils.hasText(config.getUrl())) {
            throw new IllegalStateException("DataSource '" + name + "' url is required");
        }
        if (!StringUtils.hasText(config.getUsername())) {
            throw new IllegalStateException("DataSource '" + name + "' username is required");
        }
        if (!StringUtils.hasText(config.getDriverClassName())) {
            throw new IllegalStateException("DataSource '" + name + "' driverClassName is required");
        }
        if (!StringUtils.hasText(config.getEntityPackages())) {
            log.warn("DataSource '{}' entityPackages is empty, this may cause issues with JPA entity scanning", name);
        }
    }

    /**
     * 创建数据源
     */
    private DataSource createDataSource(MultipleDataSourceProperties.DataSourceConfig config)
    {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setDriverClassName(config.getDriverClassName());
        hikariConfig.setMaximumPoolSize(config.getMaximumPoolSize());
        hikariConfig.setMinimumIdle(config.getMinimumIdle());
        hikariConfig.setConnectionTimeout(config.getConnectionTimeout());
        hikariConfig.setIdleTimeout(config.getIdleTimeout());
        hikariConfig.setMaxLifetime(config.getMaxLifetime());

        // 连接池名称
        hikariConfig.setPoolName("HikariPool-" + System.currentTimeMillis());

        return new HikariDataSource(hikariConfig);
    }

    /**
     * 创建EntityManagerFactory
     */
    private EntityManagerFactory createEntityManagerFactory(String name, DataSource dataSource,
                                                            MultipleDataSourceProperties.DataSourceConfig config)
    {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        
        // 设置实体扫描包
        if (StringUtils.hasText(config.getEntityPackages())) {
            factory.setPackagesToScan(config.getEntityPackages().split(","));
        }
        
        factory.setPersistenceUnitName(name);

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect");
        vendorAdapter.setShowSql(true);
        factory.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.format_sql", true);
        jpaProperties.put("hibernate.use_sql_comments", true);
        jpaProperties.put("hibernate.jdbc.batch_size", 50);
        jpaProperties.put("hibernate.order_inserts", true);
        jpaProperties.put("hibernate.order_updates", true);
        jpaProperties.put("hibernate.naming.physical-strategy",
                "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");

        factory.setJpaProperties(jpaProperties);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    /**
     * 创建事务管理器
     */
    private PlatformTransactionManager createTransactionManager(String name, EntityManagerFactory emf)
    {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf);
        return txManager;
    }

    /**
     * 获取数据源
     */
    public DataSource getDataSource(String name)
    {
        return dataSourceMap.get(name);
    }

    /**
     * 获取EntityManagerFactory
     */
    public EntityManagerFactory getEntityManagerFactory(String name)
    {
        return entityManagerFactoryMap.get(name);
    }

    /**
     * 获取事务管理器
     */
    public PlatformTransactionManager getTransactionManager(String name)
    {
        return transactionManagerMap.get(name);
    }

    /**
     * 获取主数据源
     */
    @Bean
    @Primary
    public DataSource primaryDataSource()
    {
        if (primaryDataSourceName == null) {
            throw new IllegalStateException("Primary datasource not initialized");
        }
        return getDataSource(primaryDataSourceName);
    }

    /**
     * 获取主EntityManagerFactory
     */
    @Bean
    @Primary
    public EntityManagerFactory primaryEntityManagerFactory()
    {
        if (primaryDataSourceName == null) {
            throw new IllegalStateException("Primary datasource not initialized");
        }
        return getEntityManagerFactory(primaryDataSourceName);
    }

    /**
     * 获取主事务管理器
     */
    @Bean
    @Primary
    public PlatformTransactionManager primaryTransactionManager()
    {
        if (primaryDataSourceName == null) {
            throw new IllegalStateException("Primary datasource not initialized");
        }
        return getTransactionManager(primaryDataSourceName);
    }

    /**
     * 获取所有数据源名称
     */
    public java.util.Set<String> getAllDataSourceNames() {
        return dataSourceMap.keySet();
    }

    /**
     * 获取主数据源名称
     */
    public String getPrimaryDataSourceName() {
        return primaryDataSourceName;
    }
}