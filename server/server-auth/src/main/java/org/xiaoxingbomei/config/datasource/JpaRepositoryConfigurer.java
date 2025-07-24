package org.xiaoxingbomei.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;

/**
 * JPA Repository动态配置器
 */
@Configuration
@Slf4j
public class JpaRepositoryConfigurer implements BeanDefinitionRegistryPostProcessor
{

    @Autowired
    private MultipleDataSourceProperties properties;
    
    private BeanDefinitionRegistry registry;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException
    {
        // 保存registry引用，在postProcessBeanFactory中使用
        this.registry = registry;
        log.info("保存BeanDefinitionRegistry引用，等待依赖注入完成后配置JPA Repository");
    }

    private void registerJpaRepositoriesForDataSource(BeanDefinitionRegistry registry,
                                                      String dataSourceName,
                                                      MultipleDataSourceProperties.DataSourceConfig config) {

        // 为每个数据源创建专门的JPA配置
        String configBeanName = dataSourceName + "JpaRepositoryConfiguration";

        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                .genericBeanDefinition(JpaRepositoryConfiguration.class)
                .addConstructorArgValue(config.getRepositoryPackages())
                .addConstructorArgValue(dataSourceName + "EntityManagerFactory")
                .addConstructorArgValue(dataSourceName + "TransactionManager");

        BeanDefinition beanDefinition = builder.getBeanDefinition();
        registry.registerBeanDefinition(configBeanName, beanDefinition);

        log.info("已为数据源 {} 注册JPA Repository配置: {}", dataSourceName, configBeanName);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        log.info("开始配置JPA Repository...");

        if (properties != null && !properties.isEmpty()) {
            properties.forEach((name, config) -> {
                log.info("为数据源 {} 配置Repository，包路径: {}", name, config.getRepositoryPackages());
                registerJpaRepositoriesForDataSource(registry, name, config);
            });
            log.info("JPA Repository配置完成");
        } else {
            log.warn("未找到多数据源配置，跳过JPA Repository配置");
        }
    }
}