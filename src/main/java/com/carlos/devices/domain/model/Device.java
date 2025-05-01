package com.carlos.devices.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record Device(@Id Integer id,
                     String name,
                     String brand,
                     DeviceState state,
                     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime creationTime) {

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

    public Device(Integer id, String name, String brand, DeviceState state) {
        this(id, name, brand, state, LocalDateTime.now());
    }
}
