package com.piotrekcieslak.gusintegrationspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // Caching SID
public class GusIntegrationSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(GusIntegrationSpringApplication.class, args);
    }
}