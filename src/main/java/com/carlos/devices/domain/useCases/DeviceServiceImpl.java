package com.carlos.devices.domain.useCases;

import com.carlos.devices.domain.DeviceRepository;
import com.carlos.devices.domain.DeviceService;
import com.carlos.devices.domain.exception.BusinessRulesException;
import com.carlos.devices.domain.exception.DataException;
import com.carlos.devices.domain.model.CreateUpdateDevice;
import com.carlos.devices.domain.model.Device;
import com.carlos.devices.domain.model.DeviceState;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device findById(Integer id) {
        Device device = deviceRepository.findById(id);
        if (device == null) {
            throw new DataException("Device not found for ID: " + id);
        }
        return device;
    }

    @Override
    public Collection<Device> findAllByBrand(String brand) {
        return deviceRepository.findByBrand(brand);
    }

    @Override
    public Collection<Device> findAllByDeviceState(DeviceState state) {
        return deviceRepository.findByState(state);
    }

    @Override
    public Collection<Device> findAll() {
        return deviceRepository.findAll();
    }

    @Override
    public Device createDevice(CreateUpdateDevice device) {
        if (!device.isValidForCreation()) {
            throw new BusinessRulesException("Invalid device details, must have a name and a brand: " + device);
        }
        return deviceRepository.create(device);
    }

    /**
     * Updates an existing device in the repository with the provided details.
     * Verifies the validity of the update request and the existence of the device in the repository.
     * Throws exceptions if the update request or operation violates business rules or if the device does not exist.
     *
     * @param id the unique identifier of the device to update
     * @param device the object containing the update details for the device
     * @throws BusinessRulesException if the update details are invalid or violate business rules
     * @throws DataException if the device does not exist in the repository
     */
    @Override
    public void updateDevice(Integer id, CreateUpdateDevice device) {
        if (!device.isValidForUpdate()) {
            throw new BusinessRulesException("Invalid device details, must have at least one non-empty field: " + device);
        }
        Device existing = deviceRepository.findById(id);
        if (existing == null) {
            throw new DataException("Device for update not found for ID: " + id);
        }
        if (existing.state().equals(DeviceState.IN_USE) & !device.isStateUpdate()) {
            throw new BusinessRulesException("Device in use, cannot be updated");
        }
        String newName = StringUtils.hasLength(device.name()) ? device.name() : existing.name();
        String newBrand = StringUtils.hasLength(device.brand())? device.brand() : existing.brand();
        DeviceState newState = device.state() != null ? device.state() : existing.state();

        deviceRepository.update(id, new CreateUpdateDevice(newName, newBrand, newState));
    }

    /**
     * Deletes a device identified by its unique ID from the repository.
     * Verifies if the device exists and ensures it is not in use before proceeding with deletion.
     * If the device does not exist or is currently in use, appropriate exceptions are thrown.
     *
     * @param id the unique identifier of the device to be deleted
     * @throws DataException if the device does not exist in the repository
     * @throws BusinessRulesException if the device is currently in use and cannot be deleted
     */
    @Override
    public void deleteDevice(Integer id) {
        Device existing = deviceRepository.findById(id);
        if (existing == null) {
            throw new DataException("Device for deletion not found for ID: " + id);
        }
        if (existing.state().equals(DeviceState.IN_USE)) {
            throw new BusinessRulesException("Device in use, cannot be deleted");
        }
        deviceRepository.delete(id);
    }
}
