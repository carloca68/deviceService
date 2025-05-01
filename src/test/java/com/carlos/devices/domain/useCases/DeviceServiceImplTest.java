package com.carlos.devices.domain.useCases;

import com.carlos.devices.domain.DeviceRepository;
import com.carlos.devices.domain.exception.BusinessRulesException;
import com.carlos.devices.domain.exception.DataException;
import com.carlos.devices.domain.model.CreateUpdateDevice;
import com.carlos.devices.domain.model.Device;
import com.carlos.devices.domain.model.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DeviceServiceImpl}.
 * This class tests the business logic in the DeviceServiceImpl class.
 */
@ExtendWith(MockitoExtension.class)
class DeviceServiceImplTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceServiceImpl deviceService;

    private Device testDevice;
    private List<Device> testDevices;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        // Initialize test data
        testDevice = new Device(1, "Test Device", "Test Brand", DeviceState.AVAILABLE, now);

        testDevices = List.of(
            new Device(1, "Device 1", "Brand A", DeviceState.AVAILABLE, now),
            new Device(2, "Device 2", "Brand B", DeviceState.IN_USE, now),
            new Device(3, "Device 3", "Brand A", DeviceState.DISABLED, now)
        );
    }

    @Test
    void findById_ShouldReturnDevice_WhenDeviceExists() {
        // Arrange
        when(deviceRepository.findById(1)).thenReturn(testDevice);

        // Act
        Device result = deviceService.findById(1);

        // Assert
        assertThat(result).isEqualTo(testDevice);
        verify(deviceRepository).findById(1);
    }

    @Test
    void findById_ShouldThrowException_WhenDeviceDoesNotExist() {
        // Arrange
        when(deviceRepository.findById(999)).thenReturn(null);

        // Act & Assert
        assertThatThrownBy(() -> deviceService.findById(999))
                .isInstanceOf(DataException.class)
                .hasMessageContaining("Device not found for ID: 999");

        verify(deviceRepository).findById(999);
    }

    @Test
    void findAllByBrand_ShouldReturnDevices_WhenBrandExists() {
        // Arrange
        List<Device> brandADevices = testDevices.stream()
                .filter(d -> "Brand A".equals(d.brand()))
                .toList();

        when(deviceRepository.findByBrand("Brand A")).thenReturn(brandADevices);

        // Act
        Collection<Device> result = deviceService.findAllByBrand("Brand A");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Device::brand).containsOnly("Brand A");
        verify(deviceRepository).findByBrand("Brand A");
    }

    @Test
    void findAllByDeviceState_ShouldReturnDevices_WhenStateExists() {
        // Arrange
        List<Device> availableDevices = testDevices.stream()
                .filter(d -> DeviceState.AVAILABLE.equals(d.state()))
                .toList();

        when(deviceRepository.findByState(DeviceState.AVAILABLE)).thenReturn(availableDevices);

        // Act
        Collection<Device> result = deviceService.findAllByDeviceState(DeviceState.AVAILABLE);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).extracting(Device::state).containsOnly(DeviceState.AVAILABLE);
        verify(deviceRepository).findByState(DeviceState.AVAILABLE);
    }

    @Test
    void findAll_ShouldReturnAllDevices() {
        // Arrange
        when(deviceRepository.findAll()).thenReturn(testDevices);

        // Act
        Collection<Device> result = deviceService.findAll();

        // Assert
        assertThat(result).hasSize(3);
        assertThat(result).isEqualTo(testDevices);
        verify(deviceRepository).findAll();
    }

    @Test
    void createDevice_ShouldCreateAndReturnDevice_WhenInputIsValid() {
        // Arrange
        CreateUpdateDevice createDevice = new CreateUpdateDevice("New Device", "New Brand", DeviceState.AVAILABLE);
        Device createdDevice = new Device(4, "New Device", "New Brand", DeviceState.AVAILABLE, now);

        when(deviceRepository.create(any(CreateUpdateDevice.class))).thenReturn(createdDevice);

        // Act
        Device result = deviceService.createDevice(createDevice);

        // Assert
        assertThat(result).isEqualTo(createdDevice);
        verify(deviceRepository).create(createDevice);
    }

    @Test
    void createDevice_ShouldThrowException_WhenInputIsInvalid() {
        // Arrange
        CreateUpdateDevice invalidDevice = new CreateUpdateDevice(null, "", null);

        // Act & Assert
        assertThatThrownBy(() -> deviceService.createDevice(invalidDevice))
                .isInstanceOf(BusinessRulesException.class)
                .hasMessageContaining("Invalid device details, must have a name and a brand");

        verify(deviceRepository, never()).create(any());
    }

    @Test
    void updateDevice_ShouldUpdateDevice_WhenInputIsValidAndDeviceExists() {
        // Arrange
        CreateUpdateDevice updateDevice = new CreateUpdateDevice("Updated Device", "Updated Brand", DeviceState.DISABLED);

        when(deviceRepository.findById(1)).thenReturn(testDevice);
        doNothing().when(deviceRepository).update(anyInt(), any(CreateUpdateDevice.class));

        // Act
        deviceService.updateDevice(1, updateDevice);

        // Assert
        verify(deviceRepository).findById(1);
        verify(deviceRepository).update(eq(1), any(CreateUpdateDevice.class));
    }

    @Test
    void updateDevice_ShouldThrowException_WhenInputIsInvalid() {
        // Arrange
        CreateUpdateDevice invalidDevice = new CreateUpdateDevice(null, null, null);

        // Act & Assert
        assertThatThrownBy(() -> deviceService.updateDevice(1, invalidDevice))
                .isInstanceOf(BusinessRulesException.class)
                .hasMessageContaining("Invalid device details, must have at least one non-empty field");

        verify(deviceRepository, never()).update(anyInt(), any());
    }

    @Test
    void updateDevice_ShouldThrowException_WhenDeviceDoesNotExist() {
        // Arrange
        CreateUpdateDevice updateDevice = new CreateUpdateDevice("Updated Device", "Updated Brand", DeviceState.DISABLED);

        when(deviceRepository.findById(999)).thenReturn(null);

        // Act & Assert
        assertThatThrownBy(() -> deviceService.updateDevice(999, updateDevice))
                .isInstanceOf(DataException.class)
                .hasMessageContaining("Device for update not found for ID: 999");

        verify(deviceRepository).findById(999);
        verify(deviceRepository, never()).update(anyInt(), any());
    }

    @Test
    void updateDevice_ShouldThrowException_WhenDeviceIsInUse() {
        // Arrange
        Device inUseDevice = new Device(2, "Device 2", "Brand B", DeviceState.IN_USE, now);
        CreateUpdateDevice updateDevice = new CreateUpdateDevice("Updated Device", "Updated Brand", DeviceState.DISABLED);

        when(deviceRepository.findById(2)).thenReturn(inUseDevice);

        // Act & Assert
        assertThatThrownBy(() -> deviceService.updateDevice(2, updateDevice))
                .isInstanceOf(BusinessRulesException.class)
                .hasMessageContaining("Device in use, cannot be updated");

        verify(deviceRepository).findById(2);
        verify(deviceRepository, never()).update(anyInt(), any());
    }

    @Test
    void deleteDevice_ShouldDeleteDevice_WhenDeviceExistsAndIsNotInUse() {
        // Arrange
        when(deviceRepository.findById(1)).thenReturn(testDevice);
        doNothing().when(deviceRepository).delete(1);

        // Act
        deviceService.deleteDevice(1);

        // Assert
        verify(deviceRepository).findById(1);
        verify(deviceRepository).delete(1);
    }

    @Test
    void deleteDevice_ShouldThrowException_WhenDeviceDoesNotExist() {
        // Arrange
        when(deviceRepository.findById(999)).thenReturn(null);

        // Act & Assert
        assertThatThrownBy(() -> deviceService.deleteDevice(999))
                .isInstanceOf(DataException.class)
                .hasMessageContaining("Device for deletion not found for ID: 999");

        verify(deviceRepository).findById(999);
        verify(deviceRepository, never()).delete(anyInt());
    }

    @Test
    void deleteDevice_ShouldThrowException_WhenDeviceIsInUse() {
        // Arrange
        Device inUseDevice = new Device(2, "Device 2", "Brand B", DeviceState.IN_USE, now);

        when(deviceRepository.findById(2)).thenReturn(inUseDevice);

        // Act & Assert
        assertThatThrownBy(() -> deviceService.deleteDevice(2))
                .isInstanceOf(BusinessRulesException.class)
                .hasMessageContaining("Device in use, cannot be deleted");

        verify(deviceRepository).findById(2);
        verify(deviceRepository, never()).delete(anyInt());
    }

    @Test
    void updateDevice_ShouldUpdateDevice_WhenDeviceIsInUseButUpdateIsStateOnly() {
        // Arrange
        Device inUseDevice = new Device(2, "Device 2", "Brand B", DeviceState.IN_USE, now);
        // Create a state-only update (only state is set, name and brand are null/empty)
        CreateUpdateDevice stateOnlyUpdate = new CreateUpdateDevice(null, null, DeviceState.AVAILABLE);

        when(deviceRepository.findById(2)).thenReturn(inUseDevice);
        doNothing().when(deviceRepository).update(anyInt(), any(CreateUpdateDevice.class));

        // Act
        deviceService.updateDevice(2, stateOnlyUpdate);

        // Assert
        verify(deviceRepository).findById(2);
        verify(deviceRepository).update(eq(2), any(CreateUpdateDevice.class));
    }
}
