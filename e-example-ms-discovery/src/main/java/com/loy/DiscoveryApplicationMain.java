package com.loy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@EnableEurekaServer
@SpringBootApplication
public class DiscoveryApplicationMain {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryApplicationMain.class, args);
    }
}
