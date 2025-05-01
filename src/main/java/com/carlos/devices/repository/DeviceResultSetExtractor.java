package com.carlos.devices.repository;

import com.carlos.devices.domain.model.Device;
import com.carlos.devices.domain.model.DeviceState;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Implementation of the {@link ResultSetExtractor} interface to extract a collection of {@link Device}
 * objects from a {@link ResultSet}.
 * <p>
 * This class is designed to map the data from a {@link ResultSet} to instances of {@link Device}
 * based on the table structure. The mapping includes fields such as the device's ID, name, brand,
 * state (mapped from {@link DeviceState}), and creation time.
 * <p>
 * The extractor is typically used in conjunction with Spring's {@link org.springframework.jdbc.core.JdbcTemplate}
 * to execute SQL queries that return results in the form of {@link Device} objects.
 * <p>
 * Note that the extracted collection will only contain a single {@link Device} object if the
 * {@link ResultSet} contains at least one record. If the {@link ResultSet} is empty, an
 * empty collection is returned.
 * <p>
 * Throws {@link IllegalArgumentException} if any of the mandatory fields in the {@link Device} object
 * (name, brand or state) are null or invalid during object construction.
 */
public class DeviceResultSetExtractor implements ResultSetExtractor<Collection<Device>> {
    @Override
    public Collection<Device> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Device> devices = new ArrayList<>();
        while (rs.next()) {
            devices.add(new Device(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("brand"),
                    DeviceState.valueOf(rs.getString("state")),
                    rs.getObject("creation_time", LocalDateTime.class)
            ));
        }
        return devices;
    }
}
