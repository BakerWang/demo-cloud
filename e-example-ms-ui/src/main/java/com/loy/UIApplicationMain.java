package com.loy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient()
public class UIApplicationMain {
    static final Log logger = LogFactory.getLog(UIApplicationMain.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(UIApplicationMain.class, args);
    }
}
