package com.loy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@EnableZuulProxy
@SpringBootApplication
@EnableOAuth2Client
public class GatewayApplicationMain {
    static final Log logger = LogFactory.getLog(GatewayApplicationMain.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(GatewayApplicationMain.class, args);
    }

}
