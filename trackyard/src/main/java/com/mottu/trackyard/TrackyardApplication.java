package com.mottu.trackyard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TrackyardApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrackyardApplication.class, args);
    }
}