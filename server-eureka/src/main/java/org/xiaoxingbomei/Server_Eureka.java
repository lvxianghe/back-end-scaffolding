package org.xiaoxingbomei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Server_Eureka
{
    public static void main(String[] args)
    {
        SpringApplication app = new SpringApplication(Server_Eureka.class);
        // app.setDefaultProperties(Collections.singletonMap("server.port", "1111"));
        app.run(args);
    }
}