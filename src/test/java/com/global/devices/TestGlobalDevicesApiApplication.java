package com.global.devices;

import org.springframework.boot.SpringApplication;

public class TestGlobalDevicesApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(GlobalDevicesApiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
