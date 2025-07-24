package org.xiaoxingbomei.config.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 多数据源配置属性类
 */
@Component
@ConfigurationProperties(prefix = "multi-datasource")
public class MultiDataSourceProperties {

    /**
     * 数据源配置映射
     * key: 数据源名称 (如: ads, ads3, rds)
     * value: 数据源配置信息
     */
    private Map<String, DataSourceConfig> dataSource;

    public Map<String, DataSourceConfig> getDataSource() {
        return dataSource;
    }

    public void setDataSource(Map<String, DataSourceConfig> dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 单个数据源配置类
     */
    public static class DataSourceConfig {
        
        // 数据库连接信息
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        
        // 关键连接池配置（可选）
        private Integer initialSize;
        private Integer maxActive;
        private Integer minIdle;
        private Long maxWait;
        
        // MyBatis配置
        private String mapperLocations;
        private String basePackages; // dao层扫描路径
        private Boolean callSettersOnNulls = true;

        // Getter and Setter methods
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public Integer getInitialSize() {
            return initialSize;
        }

        public void setInitialSize(Integer initialSize) {
            this.initialSize = initialSize;
        }

        public Integer getMaxActive() {
            return maxActive;
        }

        public void setMaxActive(Integer maxActive) {
            this.maxActive = maxActive;
        }

        public Integer getMinIdle() {
            return minIdle;
        }

        public void setMinIdle(Integer minIdle) {
            this.minIdle = minIdle;
        }

        public Long getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(Long maxWait) {
            this.maxWait = maxWait;
        }

        public String getMapperLocations() {
            return mapperLocations;
        }

        public void setMapperLocations(String mapperLocations) {
            this.mapperLocations = mapperLocations;
        }

        public String getBasePackages() {
            return basePackages;
        }

        public void setBasePackages(String basePackages) {
            this.basePackages = basePackages;
        }

        public Boolean getCallSettersOnNulls() {
            return callSettersOnNulls;
        }

        public void setCallSettersOnNulls(Boolean callSettersOnNulls) {
            this.callSettersOnNulls = callSettersOnNulls;
        }
    }
} 