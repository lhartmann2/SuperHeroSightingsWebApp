package com.sg.superherosightings.dao;

import com.sg.superherosightings.entity.Hero;
import com.sg.superherosightings.entity.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LocationDaoDB implements LocationDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Location addLocation(Location location) {
        final String ADD_LOCATION = "INSERT INTO location(locName, locDescription, " +
                "locAddress, lat, lon) VALUES(?,?,?,?,?)";
        jdbc.update(ADD_LOCATION,
                location.getName(),
                location.getDescription(),
                location.getAddress(),
                location.getLatitude(),
                location.getLongitude());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        location.setId(newId);
        return location;
    }

    @Override
    public Location getLocationById(int id) {
        try {
            final String GET_LOC_BY_ID = "SELECT * FROM location WHERE id = ?";
            return jdbc.queryForObject(GET_LOC_BY_ID, new LocationMapper(), id);
        } catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public void updateLocation(Location location) {
        final String UPDATE_LOC = "UPDATE location SET locName = ?, locDescription = ?, " +
                "locAddress = ?, lat = ?, lon = ? WHERE id = ?";
        jdbc.update(UPDATE_LOC,
                location.getName(),
                location.getDescription(),
                location.getAddress(),
                location.getLatitude(),
                location.getLongitude(),
                location.getId());
    }

    @Override
    @Transactional
    public void deleteLocationById(int id) {
        //Delete from sightings
        final String DELETE_SIGHTING_BY_LOC = "DELETE FROM sighting WHERE locId = ?";
        jdbc.update(DELETE_SIGHTING_BY_LOC, id);

        //Delete location
        final String DELETE_LOCATION = "DELETE FROM location WHERE id = ?";
        jdbc.update(DELETE_LOCATION, id);
    }

    @Override
    public List<Location> getAllLocations() {
        final String GET_ALL_LOCS = "SELECT * FROM location";
        return jdbc.query(GET_ALL_LOCS, new LocationMapper());
    }

    @Override
    public List<Location> getAllLocationsOfHero(Hero hero) {
        final String SELECT_LOCS_OF_HERO = "SELECT l.* FROM location l JOIN " +
                "sighting s ON s.locId = l.id WHERE s.heroId = ?";
        return jdbc.query(SELECT_LOCS_OF_HERO, new LocationMapper(), hero.getId());
    }

    public static final class LocationMapper implements RowMapper<Location> {
        @Override
        public Location mapRow(ResultSet rs, int index) throws SQLException {
            Location location = new Location();
            location.setId(rs.getInt("id"));
            location.setName(rs.getString("locName"));
            location.setDescription(rs.getString("locDescription"));
            location.setAddress(rs.getString("locAddress"));
            location.setLatitude(rs.getString("lat"));
            location.setLongitude(rs.getString("lon"));
            return location;
        }
    }
}
