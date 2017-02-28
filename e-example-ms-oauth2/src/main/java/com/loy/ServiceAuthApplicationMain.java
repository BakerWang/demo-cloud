package com.loy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@SessionAttributes("authorizationRequest")
@EnableResourceServer
@EnableAutoConfiguration()
@EnableEurekaClient()
@EnableJpaRepositories
@ComponentScan(basePackages = { "com.loy"})
public class ServiceAuthApplicationMain extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAuthApplicationMain.class, args);
    }

  /*  @Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
        registration.addUrlMappings("/console*//*");
        return registration;
    }*/

}
