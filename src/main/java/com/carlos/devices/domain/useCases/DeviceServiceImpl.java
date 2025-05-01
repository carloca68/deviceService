package com.carlos.devices.domain.useCases;

import com.carlos.devices.domain.DeviceRepository;
import com.carlos.devices.domain.DeviceService;
import com.carlos.devices.domain.exception.BusinessRulesException;
import com.carlos.devices.domain.model.CreateUpdateDevice;
import com.carlos.devices.domain.model.Device;
import com.carlos.devices.domain.model.DeviceState;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device findById(long id) {
        return null;
    }

    @Override
    public Collection<Device> findAllByBrand(String brand) {
        return List.of();
    }

    @Override
    public Collection<Device> findAllByDeviceState(DeviceState state) {
        return List.of();
    }

    @Override
    public Collection<Device> findAll() {
        return List.of();
    }

    @Override
    public Device createDevice(CreateUpdateDevice device) {
        if (!device.isValidForCreation()) {
            throw new BusinessRulesException("Invalid device details, must have a name and a brand: " + device.toString());
        }
        return null;
    }

    @Override
    public void updateDevice(long id, CreateUpdateDevice device) {
         if (!device.isValidForUpdate()) {
             throw new BusinessRulesException("Invalid device details, must have at least one non-empty field: " + device.toString());
         }
    }

    @Override
    public void deleteDevice(long id) {

    }
}
