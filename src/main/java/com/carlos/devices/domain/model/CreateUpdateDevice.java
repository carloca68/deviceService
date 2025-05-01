package com.carlos.devices.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record CreateUpdateDevice(String name, String brand, DeviceState state) {

    @JsonIgnore
    public boolean isValidForUpdate() {
        return (name != null && !name.isBlank()) | (brand != null && !brand.isBlank()) | state != null;
    }

    @JsonIgnore
    public boolean isValidForCreation() {
        return (name != null && !name.isBlank()) && (brand != null && !brand.isBlank());
    }

    public boolean isStateUpdate() {
        return state != null && (name == null || name.isBlank()) && (brand == null || brand.isBlank());
    }
}
