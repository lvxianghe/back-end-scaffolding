package org.xiaoxingbomei.config.mybatis;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xiaoxingbomei.interceptor.SqlPrintInterceptor;
import org.springframework.beans.factory.annotation.Autowired;


import javax.sql.DataSource;

@Configuration
@MapperScan("org.xiaoxingbomei.dao")
public class MyBatisConfig
{

    @Autowired
    private DataSource dataSource;

    @Bean
    public SqlPrintInterceptor sqlPrintInterceptor()
    {
        return new SqlPrintInterceptor();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception
    {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPlugins(sqlPrintInterceptor());
        return sessionFactory.getObject();
    }

}


