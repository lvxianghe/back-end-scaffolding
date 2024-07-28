package org.xiaoxingbomei;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableEurekaClient
@ServletComponentScan
@EnableAspectJAutoProxy
@EnableScheduling
@EnableAsync
@Slf4j
public class Server_Main
{
    public static void main(String[] args) throws UnknownHostException
    {

        //
        long start = System.currentTimeMillis();

        //
        ConfigurableApplicationContext application  = SpringApplication.run(Server_Main.class, args);
        Environment env                             = application.getEnvironment();
        String ip                                   = InetAddress.getLocalHost().getHostAddress();
        String applicationName                      = env.getProperty("spring.application.name");
        String port                                 = env.getProperty("server.port");
        String path                                 = env.getProperty("server.servlet.context-path");

        //
        if (StringUtils.isEmpty(path) || "/".equals(path))
        {
            path = "";
        }

        // 打印系统信息
        log.info("\n----------------------------------------------------------\n\t{}{}{}{}{}",
                applicationName + " is running, Access URLs:",
                "\n\t Local    访问网址: \t http://localhost:"  + port + path,
                "\n\t External 访问网址: \t http://" + ip + ":" + port + path,
                "\n\t Swagger  访问网址: \t http://" + ip + ":" + port + path + "/swagger-ui/index.html",
                "\n----------------------------------------------------------\n");
        log.info("服务启动成功!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 耗时：{} s", (System.currentTimeMillis() - start) / 1000);


    }


}

