package com.carlos.devices.domain.model;

public record CreateUpdateDevice(String name, String brand) {

    public boolean isValidForUpdate() {
        return (name != null && !name.isBlank()) | (brand != null && !brand.isBlank());
    }

    public boolean isValidForCreation() {
        return (name != null && !name.isBlank()) && (brand != null && !brand.isBlank());
    }
}
