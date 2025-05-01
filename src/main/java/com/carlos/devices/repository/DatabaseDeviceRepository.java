package com.carlos.devices.repository;

import com.carlos.devices.domain.DeviceRepository;
import com.carlos.devices.domain.exception.BusinessRulesException;
import com.carlos.devices.domain.model.CreateUpdateDevice;
import com.carlos.devices.domain.model.Device;
import com.carlos.devices.domain.model.DeviceState;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Implementation of the {@link DeviceRepository} interface that uses a relational database for
 * storing and retrieving device information. This class leverages Spring's {@link JdbcTemplate}
 * to perform database operations and adheres to the repository pattern.
 *
 * This repository handles the following operations:
 * 1. Retrieve a single device by its ID.
 * 2. Create a new device and persist it in the database.
 * 3. Update an existing device's information.
 * 4. Delete a device by its ID.
 * 5. Retrieve all devices filtered by a specific brand.
 * 6. Retrieve all devices filtered by their state.
 * 7. Retrieve all devices stored in the database.
 *
 * Transactions:
 * Methods annotated with {@link Transactional} ensure that updates, creation, and deletion
 * operations are executed within database transactions, providing rollback capability in case
 * of failures.
 *
 * Queries:
 * Predefined SQL queries are used for various database operations. These include SELECT, INSERT,
 * UPDATE, and DELETE statements that leverage parameterized queries to prevent SQL injection attacks.
 *
 * Data Mapping:
 * The {@link DeviceResultSetExtractor} class is employed to map the {@link ResultSet} returned
 * from the database into collections of {@link Device} objects. This ensures that all database rows
 * are translated into Java objects efficiently.
 *
 * Error Handling:
 * - Throws {@link BusinessRulesException} in cases where a deletion operation does not find a
 *   matching device to delete.
 * - Assumes the database always returns valid, non-null fields when retrieving devices. Invalid
 *   or null state fields, for example, may cause exceptions during object construction.
 */
@Repository
@Transactional(readOnly = true)
public class DatabaseDeviceRepository implements DeviceRepository {

    private static final String SELECT_DEVICE_QUERY = "SELECT * FROM device WHERE id = ?";
    private static final String DELETE_DEVICE_QUERY = "DELETE FROM device WHERE id = ?";
    private static final String SELECT_ALL_DEVICES_BY_BRAND_QUERY = "SELECT * FROM device WHERE brand = ?";
    private static final String SELECT_ALL_DEVICES_BY_STATE_QUERY = "SELECT * FROM device WHERE state = ?";
    private static final String SELECT_ALL_DEVICES_QUERY = "SELECT * FROM device";
    private static final String UPDATE_DEVICE_QUERY = "UPDATE device SET name = ?, brand = ?, state = ? WHERE id = ?";
    private static final String INSERT_DEVICE_QUERY = "INSERT INTO device (name, brand, state, creation_time) VALUES (?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    public DatabaseDeviceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Device findById(Integer id) {
        Collection<Device> devices = jdbcTemplate.query(SELECT_DEVICE_QUERY, new DeviceResultSetExtractor(), id);
        assert devices != null;
        if (devices.isEmpty()) {
            return null;
        }
        return devices.iterator().next();
    }

    @Override
    @Transactional()
    public void update(Integer id, CreateUpdateDevice device) {
        jdbcTemplate.update(UPDATE_DEVICE_QUERY, device.name(), device.brand(), device.state().name(), id);
    }

    @Override
    @Transactional()
    public Device create(CreateUpdateDevice device) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_DEVICE_QUERY,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, device.name());
            ps.setString(2, device.brand());
            ps.setString(3, DeviceState.AVAILABLE.name());
            ps.setObject(4, now, java.sql.Types.TIMESTAMP);
            return ps;
        }, keyHolder);

        return new Device((Integer) keyHolder.getKeyList().getFirst().get("id"), device.name(), device.brand(), DeviceState.AVAILABLE, now);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        int modified = jdbcTemplate.update(DELETE_DEVICE_QUERY, id);
        if (modified != 1) {
            throw new BusinessRulesException("Device not found for deletion: " + id);
        }
    }

    @Override
    public Collection<Device> findByBrand(String brand) {
        return jdbcTemplate.query(SELECT_ALL_DEVICES_BY_BRAND_QUERY, new DeviceResultSetExtractor(), brand);
    }

    @Override
    public Collection<Device> findByState(DeviceState state) {
        return jdbcTemplate.query(SELECT_ALL_DEVICES_BY_STATE_QUERY, new DeviceResultSetExtractor(), state.name());
    }

    @Override
    public Collection<Device> findAll() {
        return jdbcTemplate.query(SELECT_ALL_DEVICES_QUERY, new DeviceResultSetExtractor());
    }
}
