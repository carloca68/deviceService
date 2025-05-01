package com.carlos.devices.domain;

import com.carlos.devices.domain.model.CreateUpdateDevice;
import com.carlos.devices.domain.model.Device;
import com.carlos.devices.domain.model.DeviceState;

import java.util.Collection;

public interface DeviceService {
    Device findById(Integer id);

    Collection<Device> findAllByBrand(String brand);

    Collection<Device> findAllByDeviceState(DeviceState state);

    Collection<Device> findAll();

    Device createDevice(CreateUpdateDevice device);

    void updateDevice(Integer id, CreateUpdateDevice device);

    void deleteDevice(Integer id);
}
