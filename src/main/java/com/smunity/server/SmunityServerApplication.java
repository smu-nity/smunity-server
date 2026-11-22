package com.smunity.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SmunityServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmunityServerApplication.class, args);
    }

}
