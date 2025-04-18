package org.xiaoxingbomei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class ,
            com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure.class })
@EnableEurekaServer
public class Server_Eureka
{
    public static void main(String[] args)
    {
        SpringApplication app = new SpringApplication(Server_Eureka.class);
        app.run(args);
    }
}