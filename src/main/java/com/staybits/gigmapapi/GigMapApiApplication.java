package com.staybits.gigmapapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GigMapApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GigMapApiApplication.class, args);
    }

}
