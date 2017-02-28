package com;

import com.loy.e.core.repository.impl.DefaultRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by kangwang on 2017/2/28.
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAutoConfiguration()
@ComponentScan(basePackages = { "com.cupdata"})
@EnableJpaRepositories(repositoryFactoryBeanClass = DefaultRepositoryFactoryBean.class, basePackages = {
        "com.cupdata"})
@EnableCaching
@EntityScan({ "com.cupdata"})
public class ExampleApplication {

    public static void main(String[] args){
        SpringApplication.run(ExampleApplication.class,args);
    }
}
