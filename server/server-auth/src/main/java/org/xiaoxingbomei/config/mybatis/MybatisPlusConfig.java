package org.xiaoxingbomei.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xiaoxingbomei.config.datasource.SqlExecutionMonitor;

/**
 * MyBatis-Plus配置类
 */
@Configuration
@Slf4j
public class MybatisPlusConfig
{

    /**
     * MyBatis-Plus拦截器配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 1. 分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(1000L);
        paginationInnerInterceptor.setOptimizeJoin(true);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        // 2. 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        // 3. 防止全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        log.info("MyBatis-Plus拦截器配置完成：分页、乐观锁、防全表更新");
        return interceptor;
    }

    /**
     * SQL执行监控拦截器注册器
     * 使用BeanPostProcessor在SqlSessionFactory创建后自动注册拦截器
     */
    @Bean
    public BeanPostProcessor sqlInterceptorBeanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                // 当SqlSessionFactory创建完成后，自动注册SQL监控拦截器
                if (bean instanceof SqlSessionFactory) {
                    SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) bean;
                    SqlExecutionMonitor.SqlInterceptor sqlInterceptor = new SqlExecutionMonitor.SqlInterceptor();
                    sqlSessionFactory.getConfiguration().addInterceptor(sqlInterceptor);
                    log.info("🔥 SQL执行监控拦截器已注册到: {}", beanName);
                }
                return bean;
            }
        };
    }
}