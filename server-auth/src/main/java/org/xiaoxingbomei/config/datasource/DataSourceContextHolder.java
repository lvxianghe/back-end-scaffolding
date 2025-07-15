package org.xiaoxingbomei.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import java.util.Set;

/**
 * 多数据源上下文管理器
 * 提供数据源切换和管理的通用功能
 */
@Component
@Slf4j
public class DataSourceContextHolder
{

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
    
    @Autowired
    private MultiDataSourceFactory multiDataSourceFactory;

    /**
     * 设置当前线程的数据源
     */
    public static void setDataSource(String dataSourceName) {
        log.debug("切换到数据源: {}", dataSourceName);
        contextHolder.set(dataSourceName);
    }

    /**
     * 获取当前线程的数据源名称
     */
    public static String getDataSource() {
        return contextHolder.get();
    }

    /**
     * 清除当前线程的数据源设置
     */
    public static void clearDataSource() {
        contextHolder.remove();
    }

    /**
     * 获取指定名称的数据源
     */
    public DataSource getDataSource(String name) {
        return multiDataSourceFactory.getDataSource(name);
    }

    /**
     * 获取主数据源
     */
    public DataSource getPrimaryDataSource() {
        return multiDataSourceFactory.getDataSource(multiDataSourceFactory.getPrimaryDataSourceName());
    }

    /**
     * 获取指定名称的EntityManagerFactory
     */
    public EntityManagerFactory getEntityManagerFactory(String name) {
        return multiDataSourceFactory.getEntityManagerFactory(name);
    }

    /**
     * 获取主EntityManagerFactory
     */
    public EntityManagerFactory getPrimaryEntityManagerFactory() {
        return multiDataSourceFactory.getEntityManagerFactory(multiDataSourceFactory.getPrimaryDataSourceName());
    }

    /**
     * 获取指定名称的事务管理器
     */
    public PlatformTransactionManager getTransactionManager(String name) {
        return multiDataSourceFactory.getTransactionManager(name);
    }

    /**
     * 获取主事务管理器
     */
    public PlatformTransactionManager getPrimaryTransactionManager() {
        return multiDataSourceFactory.getTransactionManager(multiDataSourceFactory.getPrimaryDataSourceName());
    }

    /**
     * 获取所有数据源名称
     */
    public Set<String> getAllDataSourceNames() {
        return multiDataSourceFactory.getAllDataSourceNames();
    }

    /**
     * 获取主数据源名称
     */
    public String getPrimaryDataSourceName() {
        return multiDataSourceFactory.getPrimaryDataSourceName();
    }

    /**
     * 检查数据源是否存在
     */
    public boolean hasDataSource(String name) {
        return multiDataSourceFactory.getAllDataSourceNames().contains(name);
    }

    /**
     * 在指定数据源上下文中执行操作
     */
    public <T> T executeWithDataSource(String dataSourceName, java.util.function.Supplier<T> action) {
        String originalDataSource = getDataSource();
        try {
            setDataSource(dataSourceName);
            return action.get();
        } finally {
            if (originalDataSource != null) {
                setDataSource(originalDataSource);
            } else {
                clearDataSource();
            }
        }
    }

    /**
     * 在指定数据源上下文中执行操作（无返回值）
     */
    public void executeWithDataSource(String dataSourceName, Runnable action) {
        executeWithDataSource(dataSourceName, () -> {
            action.run();
            return null;
        });
    }
} 