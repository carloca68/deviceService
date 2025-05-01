package com.carlos.devices.repository;

import com.carlos.devices.domain.exception.BusinessRulesException;
import com.carlos.devices.domain.model.CreateUpdateDevice;
import com.carlos.devices.domain.model.Device;
import com.carlos.devices.domain.model.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration tests for {@link DatabaseDeviceRepository}.
 * These tests use a real PostgreSQL database running in a Docker container via Testcontainers.
 */
@SpringBootTest(classes = com.carlos.app.DevicesApiApplication.class)
@Import(RepositoryTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
class DatabaseDeviceRepositoryTest {

    @Autowired
    private DatabaseDeviceRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Device testDevice1;
    private Device testDevice2;
    private Device testDevice3;

    @BeforeEach
    void setUp() {
        // Clean up the database before each test
        jdbcTemplate.update("DELETE FROM device");

        // Insert test data
        LocalDateTime now = LocalDateTime.now();

        // Insert first test device
        jdbcTemplate.update(
                "INSERT INTO device (id, name, brand, state, creation_time) VALUES (?, ?, ?, ?, ?)",
                1, "Test Device 1", "Brand A", DeviceState.AVAILABLE.name(), now
        );
        testDevice1 = new Device(1, "Test Device 1", "Brand A", DeviceState.AVAILABLE, now);

        // Insert second test device
        jdbcTemplate.update(
                "INSERT INTO device (id, name, brand, state, creation_time) VALUES (?, ?, ?, ?, ?)",
                2, "Test Device 2", "Brand B", DeviceState.IN_USE.name(), now
        );
        testDevice2 = new Device(2, "Test Device 2", "Brand B", DeviceState.IN_USE, now);

        // Insert third test device
        jdbcTemplate.update(
                "INSERT INTO device (id, name, brand, state, creation_time) VALUES (?, ?, ?, ?, ?)",
                3, "Test Device 3", "Brand A", DeviceState.DISABLED.name(), now
        );
        testDevice3 = new Device(3, "Test Device 3", "Brand A", DeviceState.DISABLED, now);

        // Reset the sequence to ensure new IDs start after our test data
        jdbcTemplate.queryForObject("SELECT setval('device_id_seq', 3, true)", Integer.class);
    }

    @Test
    void findById_ShouldReturnDevice_WhenDeviceExists() {
        // Act
        Device device = repository.findById(1);

        // Assert
        assertThat(device).isNotNull();
        assertThat(device.id()).isEqualTo(1);
        assertThat(device.name()).isEqualTo("Test Device 1");
        assertThat(device.brand()).isEqualTo("Brand A");
        assertThat(device.state()).isEqualTo(DeviceState.AVAILABLE);
    }

    @Test
    void findById_ShouldReturnNull_WhenDeviceDoesNotExist() {
        // Act
        Device device = repository.findById(999);

        // Assert
        assertThat(device).isNull();
    }

    @Test
    void update_ShouldUpdateDevice_WhenDeviceExists() {
        // Arrange
        CreateUpdateDevice updateDevice = new CreateUpdateDevice("Updated Device", "Updated Brand", DeviceState.IN_USE);

        // Act
        repository.update(1, updateDevice);

        // Assert
        Device updatedDevice = repository.findById(1);
        assertThat(updatedDevice).isNotNull();
        assertThat(updatedDevice.name()).isEqualTo("Updated Device");
        assertThat(updatedDevice.brand()).isEqualTo("Updated Brand");
        assertThat(updatedDevice.state()).isEqualTo(DeviceState.IN_USE);
    }

    @Test
    void create_ShouldCreateNewDevice() {
        // Arrange
        CreateUpdateDevice newDevice = new CreateUpdateDevice("New Device", "New Brand", DeviceState.AVAILABLE);

        // Act
        Device createdDevice = repository.create(newDevice);

        // Assert
        assertThat(createdDevice).isNotNull();
        assertThat(createdDevice.id()).isGreaterThan(3); // Should be greater than our test data IDs
        assertThat(createdDevice.name()).isEqualTo("New Device");
        assertThat(createdDevice.brand()).isEqualTo("New Brand");
        assertThat(createdDevice.state()).isEqualTo(DeviceState.AVAILABLE);
        assertThat(createdDevice.creationTime()).isNotNull();

        // Verify it's in the database
        Device retrievedDevice = repository.findById(createdDevice.id());
        assertThat(retrievedDevice).isNotNull();
        assertThat(retrievedDevice.name()).isEqualTo("New Device");
    }

    @Test
    void delete_ShouldDeleteDevice_WhenDeviceExists() {
        // Act
        repository.delete(1);

        // Assert
        Device deletedDevice = repository.findById(1);
        assertThat(deletedDevice).isNull();
    }

    @Test
    void delete_ShouldThrowException_WhenDeviceDoesNotExist() {
        // Act & Assert
        assertThatThrownBy(() -> repository.delete(999))
                .isInstanceOf(BusinessRulesException.class)
                .hasMessageContaining("Device not found for deletion: 999");
    }

    @Test
    void findByBrand_ShouldReturnDevices_WhenBrandExists() {
        // Act
        Collection<Device> devices = repository.findByBrand("Brand A");

        // Assert
        assertThat(devices).hasSize(2);
        assertThat(devices).extracting(Device::brand).containsOnly("Brand A");
    }

    @Test
    void findByBrand_ShouldReturnEmptyCollection_WhenBrandDoesNotExist() {
        // Act
        Collection<Device> devices = repository.findByBrand("Non-existent Brand");

        // Assert
        assertThat(devices).isEmpty();
    }

    @Test
    void findByState_ShouldReturnDevices_WhenStateExists() {
        // Act
        Collection<Device> devices = repository.findByState(DeviceState.AVAILABLE);

        // Assert
        assertThat(devices).hasSize(1);
        assertThat(devices).extracting(Device::state).containsOnly(DeviceState.AVAILABLE);
    }

    @Test
    void findByState_ShouldReturnEmptyCollection_WhenNoDevicesHaveState() {
        // Arrange
        jdbcTemplate.update("DELETE FROM device");

        // Act
        Collection<Device> devices = repository.findByState(DeviceState.AVAILABLE);

        // Assert
        assertThat(devices).isEmpty();
    }

    @Test
    void findAll_ShouldReturnAllDevices() {
        // Act
        Collection<Device> devices = repository.findAll();

        // Assert
        assertThat(devices).hasSize(3);
        assertThat(devices).extracting(Device::id).containsExactlyInAnyOrder(1, 2, 3);
    }

    @Test
    void findAll_ShouldReturnEmptyCollection_WhenNoDevicesExist() {
        // Arrange
        jdbcTemplate.update("DELETE FROM device");

        // Act
        Collection<Device> devices = repository.findAll();

        // Assert
        assertThat(devices).isEmpty();
    }
}
