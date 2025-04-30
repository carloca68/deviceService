package com.carlos.devices.controller;

import com.carlos.devices.domain.model.Device;
import com.carlos.devices.domain.model.DeviceState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/device")
public class DeviceRestController {

    private final Logger logger = LoggerFactory.getLogger(DeviceRestController.class);

    @GetMapping("/{id}")
    public Device findById(@PathVariable long id) {
        return new Device(1L, "Test", "Brand", DeviceState.AVAILABLE, LocalDateTime.now());
    }
}
