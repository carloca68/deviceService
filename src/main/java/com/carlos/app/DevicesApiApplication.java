package com.carlos.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.carlos")
public class DevicesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevicesApiApplication.class, args);
    }

}
