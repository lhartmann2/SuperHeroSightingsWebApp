package com.sg.superherosightings.dao;

import com.sg.superherosightings.entity.Hero;
import com.sg.superherosightings.entity.Location;
import com.sg.superherosightings.entity.Sighting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class SightingDaoDB implements SightingDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Sighting addSighting(Sighting sighting) {
        final String ADD_SIGHTING = "INSERT INTO sighting(heroId, locId, sightingDate) " +
                "VALUES(?,?,?)";
        jdbc.update(ADD_SIGHTING,
                sighting.getHeroId(),
                sighting.getLocationId(),
                sighting.getSightingDate());
        associateHeroAndLocation(sighting);
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        sighting.setId(newId);
        return sighting;
    }

    @Override
    public List<Sighting> getSightingByHero(Hero hero) {
        final String GET_SIGHTINGS_BY_HERO = "SELECT * FROM sighting WHERE heroId = ?";
        List<Sighting> sightings = jdbc.query(GET_SIGHTINGS_BY_HERO, new SightingMapper(), hero.getId());
        for(Sighting s : sightings) {
            associateHeroAndLocation(s);
        }
        return sightings;
    }

    @Override
    public List<Sighting> getSightingByLocation(Location location) {
        final String GET_SIGHTINGS_BY_LOCATION = "SELECT * FROM sighting WHERE locId = ?";
        List<Sighting> sightings = jdbc.query(GET_SIGHTINGS_BY_LOCATION, new SightingMapper(), location.getId());
        for(Sighting s : sightings) {
            associateHeroAndLocation(s);
        }
        return sightings;
    }

    @Override
    public List<Sighting> getSightingByDate(java.util.Date date) {
        final String GET_SIGHTINGS_BY_DATE = "SELECT * FROM sighting WHERE sightingDate = ?";
        List<Sighting> sightings = jdbc.query(GET_SIGHTINGS_BY_DATE, new SightingMapper(), date);
        for(Sighting s : sightings) {
            associateHeroAndLocation(s);
        }
        return sightings;
    }

    @Override
    public List<Sighting> getAllSightings() {
        List<Sighting> sightings = jdbc.query("SELECT * FROM sighting", new SightingMapper());
        for(Sighting s : sightings) {
            associateHeroAndLocation(s);
        }
        return sightings;
    }

    @Override
    public Sighting getSightingById(int id) {
        try {
            final String GET_SIGHTING_BY_ID = "SELECT * FROM sighting WHERE id = ?";
            Sighting sighting = jdbc.queryForObject(GET_SIGHTING_BY_ID, new SightingMapper(), id);
            associateHeroAndLocation(sighting);
            return sighting;
        } catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Sighting> getTenRecentSightings() {
        final String TEN_MOST_RECENT = "SELECT * FROM sighting ORDER BY id DESC LIMIT 10";
        List<Sighting> sightings = jdbc.query(TEN_MOST_RECENT, new SightingMapper());
        for(Sighting s : sightings) {
            associateHeroAndLocation(s);
        }
        return sightings;
    }

    @Override
    @Transactional
    public void deleteSightingById(int id) {
        final String DELETE_BY_ID = "DELETE FROM sighting WHERE id = ?";
        jdbc.update(DELETE_BY_ID, id);
    }

    @Override
    public void updateSighting(Sighting sighting) {
        final String UPDATE_SIGHTING = "UPDATE sighting SET heroId = ?, locId = ?, "+
                "sightingDate = ? WHERE id = ?";
        jdbc.update(UPDATE_SIGHTING,
                sighting.getHeroId(),
                sighting.getLocationId(),
                sighting.getSightingDate(),
                sighting.getId());
    }

    private void associateHeroAndLocation(Sighting sighting) {
        final String SELECT_HERO = "SELECT * FROM hero WHERE id = ?";
        sighting.setHero(jdbc.queryForObject(SELECT_HERO, new HeroDaoDB.HeroMapper(), sighting.getHeroId()));

        final String SELECT_LOC = "SELECT * FROM location WHERE id = ?";
        sighting.setLocation(jdbc.queryForObject(SELECT_LOC, new LocationDaoDB.LocationMapper(), sighting.getLocationId()));
    }

    public static final class SightingMapper implements RowMapper<Sighting> {
        @Override
        public Sighting mapRow(ResultSet rs, int index) throws SQLException {
            Sighting sighting = new Sighting();
            sighting.setId(rs.getInt("id"));
            sighting.setHeroId(rs.getInt("heroId"));
            sighting.setLocationId(rs.getInt("locId"));
            sighting.setSightingDate(rs.getTimestamp("sightingDate"));
            return sighting;
        }
    }
}