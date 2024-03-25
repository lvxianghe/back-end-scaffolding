package org.xiaoxingbomei.config.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;

/**
 * 本地数据库连接配置
 * @Author lvxianghe
 */
@Configuration
@MapperScan(basePackages = "org.xiaoxingbomei.dao.localhost", sqlSessionTemplateRef = "localhostSqlSessionTemplate")
public class LocalHostDatabaseConfig
{
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.localhost")
    public DataSource localhostDataSource()
    {
        return DataSourceBuilder.create().build();
    }


    @Bean
    @DependsOn("localhostDataSource")
    public SqlSessionFactory localhostSqlSessionFactory() throws Exception
    {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(localhostDataSource());

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCallSettersOnNulls(true);
        factoryBean.setConfiguration(configuration);
        return factoryBean.getObject();
    }


    @Bean
    @DependsOn("localhostSqlSessionFactory")
    public SqlSessionTemplate localhostSqlSessionTemplate() throws Exception
    {
        SqlSessionTemplate template = new SqlSessionTemplate(localhostSqlSessionFactory());
        return template;
    }


}