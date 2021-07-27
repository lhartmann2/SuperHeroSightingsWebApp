package com.sg.superherosightings.dao;

import com.sg.superherosightings.entity.SuperPower;
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
public class SuperPowerDaoDB implements SuperPowerDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public SuperPower addSuperPower(SuperPower superPower) {
        final String ADD_POWER = "INSERT INTO superPower(superPowerName) VALUES(?)";
        jdbc.update(ADD_POWER,
                superPower.getSuperPowerName());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        superPower.setId(newId);
        return superPower;
    }

    @Override
    public SuperPower getSuperPowerById(int id) {
        try {
            final String GET_POWER_BY_ID = "SELECT * FROM superPower WHERE id = ?";
            return jdbc.queryForObject(GET_POWER_BY_ID, new superPowerMapper(), id);
        } catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public void updateSuperPower(SuperPower superPower) {
        final String UPDATE_POWER = "UPDATE superPower SET superPowerName = ? WHERE id = ?";
        jdbc.update(UPDATE_POWER, superPower.getSuperPowerName(), superPower.getId());
    }

    @Override
    @Transactional
    public void deleteSuperPowerById(int id) {
        final String DELETE_HERO_WITH_POWER = "DELETE FROM hero WHERE superPowerId = ?";
        jdbc.update(DELETE_HERO_WITH_POWER, id);

        final String DELETE_POWER = "DELETE FROM superPOwer WHERE id = ?";
        jdbc.update(DELETE_POWER, id);
    }

    @Override
    public List<SuperPower> getAllSuperPowers() {
        return jdbc.query("SELECT * FROM superPower", new superPowerMapper());
    }

    public static final class superPowerMapper implements RowMapper<SuperPower> {
        @Override
        public SuperPower mapRow(ResultSet rs, int index) throws SQLException {
            SuperPower superPower = new SuperPower();
            superPower.setId(rs.getInt("id"));
            superPower.setSuperPowerName(rs.getString("superPowerName"));
            return superPower;
        }
    }
}
