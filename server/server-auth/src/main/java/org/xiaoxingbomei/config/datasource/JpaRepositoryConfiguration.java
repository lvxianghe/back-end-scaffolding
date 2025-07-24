package org.xiaoxingbomei.config.datasource;

import lombok.extern.slf4j.Slf4j;

/**
 * JPA Repository配置数据载体类
 */
@Slf4j
public class JpaRepositoryConfiguration
{

    private final String basePackages;
    private final String entityManagerFactoryRef;
    private final String transactionManagerRef;

    public JpaRepositoryConfiguration(String basePackages, 
                                    String entityManagerFactoryRef, 
                                    String transactionManagerRef) {
        this.basePackages = basePackages;
        this.entityManagerFactoryRef = entityManagerFactoryRef;
        this.transactionManagerRef = transactionManagerRef;
        
        log.info("创建JPA Repository配置 - 包路径: {}, EntityManager: {}, TransactionManager: {}", 
                basePackages, entityManagerFactoryRef, transactionManagerRef);
    }

    public String getBasePackages() {
        return basePackages;
    }

    public String getEntityManagerFactoryRef() {
        return entityManagerFactoryRef;
    }

    public String getTransactionManagerRef() {
        return transactionManagerRef;
    }
} 