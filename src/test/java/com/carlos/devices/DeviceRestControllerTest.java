package com.carlos.devices;

import com.carlos.devices.domain.DeviceService;
import com.carlos.devices.domain.exception.BusinessRulesException;
import com.carlos.devices.domain.model.CreateUpdateDevice;
import com.carlos.devices.domain.model.Device;
import com.carlos.devices.domain.model.DeviceState;
import com.carlos.devices.repository.ApiExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link DeviceRestController} using Spring MVC test framework.
 * This class tests the REST endpoints of the DeviceRestController.
 * 
 * The test uses {@code @WebMvcTest} to focus only on the web layer, and provides a mock
 * {@link DeviceService} through a configuration class. This approach avoids using the
 * deprecated {@code @MockBean} annotation in Spring Boot 3.4+.
 * 
 * Each test method sets up the mock behavior for the specific test case and verifies
 * the controller's response using MockMvc.
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(DeviceRestController.class)
@Import({DeviceRestController.class, ApiExceptionHandler.class, DeviceRestControllerTest.TestConfig.class})
public class DeviceRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Configuration
    static class TestConfig {
        @Bean
        public DeviceService deviceService() {
            return mock(DeviceService.class);
        }
    }

    private Device testDevice;
    private List<Device> testDevices;

    @BeforeEach
    void setUp() {
        // Reset mock
        reset(deviceService);

        // Initialize test data
        testDevice = new Device(1, "Test Device", "Test Brand", DeviceState.AVAILABLE, LocalDateTime.now());

        testDevices = List.of(
            new Device(1, "Device 1", "Brand A", DeviceState.AVAILABLE, LocalDateTime.now()),
            new Device(2, "Device 2", "Brand B", DeviceState.IN_USE, LocalDateTime.now()),
            new Device(3, "Device 3", "Brand A", DeviceState.DISABLED, LocalDateTime.now())
        );
    }

    @Test
    void findById_ShouldReturnDevice() throws Exception {
        // Arrange
        when(deviceService.findById(1)).thenReturn(testDevice);

        // Act & Assert
        mockMvc.perform(get("/api/device/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Device")))
                .andExpect(jsonPath("$.brand", is("Test Brand")))
                .andExpect(jsonPath("$.state", is("AVAILABLE")));
    }

    @Test
    void findByBrand_ShouldReturnDevices() throws Exception {
        // Arrange
        List<Device> brandADevices = testDevices.stream()
                .filter(d -> "Brand A".equals(d.brand()))
                .toList();

        when(deviceService.findAllByBrand("Brand A")).thenReturn(brandADevices);

        // Act & Assert
        mockMvc.perform(get("/api/device/brand/Brand A"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].brand", is("Brand A")))
                .andExpect(jsonPath("$[1].brand", is("Brand A")));
    }

    @Test
    void findByState_ShouldReturnDevices() throws Exception {
        // Arrange
        List<Device> availableDevices = testDevices.stream()
                .filter(d -> DeviceState.AVAILABLE.equals(d.state()))
                .toList();

        when(deviceService.findAllByDeviceState(DeviceState.AVAILABLE)).thenReturn(availableDevices);

        // Act & Assert
        mockMvc.perform(get("/api/device/state/AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].state", is("AVAILABLE")));
    }

    @Test
    void findAll_ShouldReturnAllDevices() throws Exception {
        // Arrange
        when(deviceService.findAll()).thenReturn(testDevices);

        // Act & Assert
        mockMvc.perform(get("/api/device"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)));
    }

    @Test
    void create_ShouldCreateAndReturnDevice() throws Exception {
        // Arrange
        CreateUpdateDevice createDevice = new CreateUpdateDevice("New Device", "New Brand", DeviceState.AVAILABLE);
        Device createdDevice = new Device(4, "New Device", "New Brand", DeviceState.AVAILABLE, LocalDateTime.now());

        when(deviceService.createDevice(any(CreateUpdateDevice.class))).thenReturn(createdDevice);

        // Act & Assert
        mockMvc.perform(post("/api/device")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDevice)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.name", is("New Device")))
                .andExpect(jsonPath("$.brand", is("New Brand")))
                .andExpect(jsonPath("$.state", is("AVAILABLE")));
    }

    @Test
    void update_ShouldUpdateDevice() throws Exception {
        // Arrange
        CreateUpdateDevice updateDevice = new CreateUpdateDevice("Updated Device", "Updated Brand", null);

        doNothing().when(deviceService).updateDevice(anyInt(), any(CreateUpdateDevice.class));

        // Act & Assert
        mockMvc.perform(put("/api/device/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDevice)))
                .andExpect(status().isNoContent());

        verify(deviceService, times(1)).updateDevice(eq(1), any(CreateUpdateDevice.class));
    }

    @Test
    void updateWithInvalidData_ShouldNotUpdateDevice() throws Exception {
        // Arrange
        CreateUpdateDevice updateDevice = new CreateUpdateDevice("Updated Device", "Updated Brand", null);

        doThrow(new BusinessRulesException("Invalid data")).when(deviceService).updateDevice(anyInt(), any(CreateUpdateDevice.class));

        // Act & Assert
        mockMvc.perform(put("/api/device/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDevice)))
                .andExpect(status().isBadRequest());

        verify(deviceService).updateDevice(eq(1), any(CreateUpdateDevice.class));
    }

    @Test
    void delete_ShouldDeleteDevice() throws Exception {
        // Arrange
        doNothing().when(deviceService).deleteDevice(anyInt());

        // Act & Assert
        mockMvc.perform(delete("/api/device/1"))
                .andExpect(status().isOk());

        verify(deviceService, times(1)).deleteDevice(1);
    }
}
