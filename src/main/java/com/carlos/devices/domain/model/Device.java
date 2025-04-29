package com.carlos.devices.domain.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record Device(@Id Long id, String name, String brand, DeviceState state, LocalDateTime creationTime) {

    public Device {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be null or blank");
        }
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null");
        }

    }

    public Device(Long id, String name, String brand, DeviceState state) {
        this(id, name, brand, state, LocalDateTime.now());
    }
}
