package com.StayHere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class StayHereApplication {

    public static void main(String[] args) {
        SpringApplication.run(StayHereApplication.class, args);
    }

}

