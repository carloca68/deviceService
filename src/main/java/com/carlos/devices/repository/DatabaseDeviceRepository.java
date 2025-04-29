package com.carlos.devices.repository;

import com.carlos.devices.domain.DeviceRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseDeviceRepository implements DeviceRepository {



    private final JdbcTemplate jdbcTemplate;

    public DatabaseDeviceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
