package com.carlos.devices.domain;

import com.carlos.devices.domain.model.CreateUpdateDevice;
import com.carlos.devices.domain.model.Device;
import com.carlos.devices.domain.model.DeviceState;

import java.util.Collection;

public interface DeviceRepository {
    Device findById(Integer id);

    void update(Integer id, CreateUpdateDevice device);

    Device create(CreateUpdateDevice device);

    void delete(Integer id);

    Collection<Device> findByBrand(String brand);

    Collection<Device> findByState(DeviceState state);

    Collection<Device> findAll();
}
