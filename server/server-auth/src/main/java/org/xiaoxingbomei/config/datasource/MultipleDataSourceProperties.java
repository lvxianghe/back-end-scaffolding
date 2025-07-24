package org.xiaoxingbomei.config.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源配置属性类
 * 直接继承HashMap，支持 datasource.multiple.ysx.url 这样的配置路径
 */
@ConfigurationProperties(prefix = "datasource.multiple")
@Data
public class MultipleDataSourceProperties extends HashMap<String, MultipleDataSourceProperties.DataSourceConfig>
{
    @Data
    public static class DataSourceConfig
    {
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private String entityPackages;
        private String repositoryPackages;
        private boolean primary = false;

        // 连接池配置
        private Integer maximumPoolSize = 10;
        private Integer minimumIdle = 5;
        private Long connectionTimeout = 30000L;
        private Long idleTimeout = 600000L;
        private Long maxLifetime = 1800000L;
    }

    /**
     * 获取所有数据源配置
     */
    public Map<String, DataSourceConfig> getDataSources()
    {
        return this;
    }

    /**
     * 获取主数据源配置
     */
    public DataSourceConfig getPrimaryDataSource()
    {
        return this.values().stream()
                .filter(DataSourceConfig::isPrimary)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No primary datasource configured"));
    }

    /**
     * 获取指定数据源配置
     */
    public DataSourceConfig getDataSource(String name)
    {
        return this.get(name);
    }

    /**
     * 获取主数据源名称
     */
    public String getPrimaryDataSourceName()
    {
        return this.entrySet().stream()
                .filter(entry -> entry.getValue().isPrimary())
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No primary datasource found"));
    }
}