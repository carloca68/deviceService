package com.carlos.devices;

import com.carlos.devices.domain.DeviceService;
import com.carlos.devices.domain.model.CreateUpdateDevice;
import com.carlos.devices.domain.model.Device;
import com.carlos.devices.domain.model.DeviceState;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * REST controller for managing devices.
 * Provides endpoints for performing CRUD operations on devices.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Devices API",
                version = "1.0",
                description = "API for devices management"
        )
)
@Tag(name = "Devices API", description = "Device related resources")
@RestController
@RequestMapping("/api/device")
public class DeviceRestController {

    private final Logger logger = LoggerFactory.getLogger(DeviceRestController.class);
    private final DeviceService deviceService;

    public DeviceRestController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * Retrieves a device by its unique identifier.
     *
     * @param id the unique identifier of the device to be retrieved
     * @return the {@code Device} instance corresponding to the provided identifier
     */
    @Operation(summary = "Get a device by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Device.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Device not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public Device findById(@PathVariable long id) {
        return deviceService.findById(id);
    }

    /**
     * Retrieves a collection of devices filtered by the specified brand.
     *
     * @param brand the brand name used to filter the devices; must not be null or blank
     * @return a collection of {@code Device} instances that match the specified brand
     */
    @Operation(summary = "Get all devices by brand")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Device.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @GetMapping("brand/{brand}")
    public Collection<Device> findByBrand(@PathVariable String brand) {
        return deviceService.findAllByBrand(brand);
    }

    /**
     * Retrieves a collection of devices filtered by the specified state.
     *
     * @param state the state used to filter the devices; must not be null
     * @return a collection of {@code Device} instances that match the specified state
     */
    @Operation(summary = "Get all devices by state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Device.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @GetMapping("/state/{state}")
    public Collection<Device> findByState(@PathVariable DeviceState state) {
        return deviceService.findAllByDeviceState(state);
    }

    /**
     * Retrieves a collection of all devices.
     *
     * @return a collection of {@code Device} instances representing all devices
     */
    @Operation(summary = "Get all devices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Device.class)))})})
    @GetMapping()
    public Collection<Device> findAll() {
        return deviceService.findAll();
    }

    /**
     * Creates a new device.
     *
     * @param device the {@code Device} object containing the details of the device to be created
     * @return the created {@code Device} instance
     */
    @Operation(summary = "Create a new device with the status AVAILABLE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Device.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Device create(@RequestBody CreateUpdateDevice device) {
        return deviceService.createDevice(device);
    }

    /**
     * Updates the details of an existing device identified by its unique identifier.
     *
     * @param id     the unique identifier of the device to be updated
     * @param device the {@code Device} object containing the updated details of the device
     * @return the updated {@code Device} instance
     */
    @Operation(summary = "Update a device brand or/and name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Updated",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Device update(@PathVariable long id, @RequestBody CreateUpdateDevice device) {
        deviceService.updateDevice(id, device);
        return null;
    }

    /**
     * Deletes a device identified by its unique identifier.
     *
     * @param id the unique identifier of the device to be deleted
     */
    @Operation(summary = "Delete a device by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Device not found for deletion",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        deviceService.deleteDevice(id);
    }


}
