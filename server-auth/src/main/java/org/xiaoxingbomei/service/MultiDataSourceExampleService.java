package org.xiaoxingbomei.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoxingbomei.config.datasource.DataSourceContextHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

/**
 * 多数据源使用示例服务
 * 演示如何在实际业务中使用多数据源管理器
 */
@Service
@Slf4j
public class MultiDataSourceExampleService
{

    @Autowired
    private DataSourceContextHolder dataSourceContextHolder;

    /**
     * 获取所有数据源信息
     */
    public void showAllDataSources()
    {
        log.info("=== 多数据源信息 ===");
        
        // 获取主数据源名称
        String primaryName = dataSourceContextHolder.getPrimaryDataSourceName();
        log.info("主数据源: {}", primaryName);
        
        // 获取所有数据源名称
        Set<String> allNames = dataSourceContextHolder.getAllDataSourceNames();
        log.info("所有数据源: {}", allNames);
        
        // 测试每个数据源的连接
        for (String name : allNames) {
            testDataSourceConnection(name);
        }
    }

    /**
     * 测试数据源连接
     */
    public void testDataSourceConnection(String dataSourceName)
    {
        try
        {
            if (!dataSourceContextHolder.hasDataSource(dataSourceName))
            {
                log.warn("数据源 {} 不存在", dataSourceName);
                return;
            }
            
            DataSource dataSource = dataSourceContextHolder.getDataSource(dataSourceName);
            try (Connection connection = dataSource.getConnection())
            {
                String url = connection.getMetaData().getURL();
                String username = connection.getMetaData().getUserName();
                log.info("数据源 {} 连接成功 - URL: {}, Username: {}", dataSourceName, url, username);
            }
        } catch (SQLException e)
        {
            log.error("数据源 {} 连接失败: {}", dataSourceName, e.getMessage());
        }
    }

    /**
     * 在指定数据源上下文中执行业务操作
     */
    public void executeBusinessLogic(String dataSourceName)
    {
        dataSourceContextHolder.executeWithDataSource(dataSourceName, () ->
        {
            log.info("在数据源 {} 上执行业务逻辑", dataSourceName);
            // 这里可以调用DAO层方法，会自动使用指定的数据源
            // 例如：userRepository.findAll();
        });
    }

    /**
     * 示例：在不同数据源间切换执行操作
     */
    public void switchDataSourceExample()
    {
        Set<String> allDataSources = dataSourceContextHolder.getAllDataSourceNames();
        
        for (String dataSourceName : allDataSources)
        {
            dataSourceContextHolder.executeWithDataSource(dataSourceName, () ->
            {
                log.info("当前正在使用数据源: {}", dataSourceName);
                // 在这里执行具体的数据操作
                // 例如：查询用户、保存数据等
            });
        }
    }

    /**
     * 示例：返回值的操作
     */
    public String getDataFromSpecificDataSource(String dataSourceName)
    {
        return dataSourceContextHolder.executeWithDataSource(dataSourceName, () -> {
            // 模拟从数据库查询数据
            return "从数据源 " + dataSourceName + " 获取的数据";
        });
    }
} 