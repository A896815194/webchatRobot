package com.web.webchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class WebchatRobotApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebchatRobotApplication.class, args);
    }

}
