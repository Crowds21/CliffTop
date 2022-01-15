package com.springclifftop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:config/config.properties",
        "file:${spring.profiles.path}/config.properties"},
        ignoreResourceNotFound = true)
public class SpringCliffTopApplication {

    public static void main(String[] args) {
        //new SpringApplicationBuilder().sources(SpringCliffTopApplication.class).web(WebApplicationType.NONE).run(args);
        SpringApplication.run(SpringCliffTopApplication.class, args);
    }

}
