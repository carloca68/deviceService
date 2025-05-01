package com.carlos.devices.repository;

import com.carlos.devices.domain.DeviceRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class DatabaseDeviceRepository implements DeviceRepository {


    private final JdbcTemplate jdbcTemplate;

    public DatabaseDeviceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
