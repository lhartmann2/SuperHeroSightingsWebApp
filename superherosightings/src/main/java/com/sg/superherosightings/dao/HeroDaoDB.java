package com.sg.superherosightings.dao;

import com.sg.superherosightings.entity.Hero;
import com.sg.superherosightings.entity.Location;
import com.sg.superherosightings.entity.Organization;
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
public class HeroDaoDB implements HeroDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Hero addHero(Hero hero) {
        final String INSERT_HERO = "INSERT INTO hero(heroName, heroDescription, superPowerId) " +
                "VALUES(?,?,?)";
        jdbc.update(INSERT_HERO,
                hero.getName(),
                hero.getDescription(),
                hero.getSuperPower().getId());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        hero.setId(newId);
        return hero;
    }

    @Override
    public Hero getHeroById(int id) {
        try {
            final String GET_HERO = "SELECT * FROM hero WHERE id = ?";
            Hero hero = jdbc.queryForObject(GET_HERO, new HeroMapper(), id);
            hero.setSuperPower(getPowerForHero(id));
            return hero;
        } catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public void updateHero(Hero hero) {
        final String UPDATE_HERO = "UPDATE hero SET heroName = ?, heroDescription = ?, " +
                "superPowerId = ? WHERE id = ?";
        jdbc.update(UPDATE_HERO,
                hero.getName(),
                hero.getDescription(),
                hero.getSuperPower().getId(),
                hero.getId());
    }

    @Override
    @Transactional
    public void deleteHeroById(int id) {
        //Delete Sightings
        final String DELETE_HERO_SIGHTINGS = "DELETE FROM sighting WHERE heroId = ?";
        jdbc.update(DELETE_HERO_SIGHTINGS, id);

        //Delete org_hero
        final String DELETE_ORG_HERO = "DELETE FROM org_hero WHERE heroId = ?";
        jdbc.update(DELETE_ORG_HERO, id);

        //Delete HERO
        final String DELETE_HERO = "DELETE FROM hero WHERE id = ?";
        jdbc.update(DELETE_HERO, id);
    }

    @Override
    public List<Hero> getAllHeroes() {
        final String GET_ALL_HEROES = "SELECT * FROM hero";
        List<Hero> heroes = jdbc.query(GET_ALL_HEROES, new HeroMapper());
        for(Hero h : heroes) {
            h.setSuperPower(getPowerForHero(h.getId()));
        }
        return heroes;
    }

    @Override
    public List<Hero> listHeroesInOrg(Organization organization) {
        final String LIST_HEROES_IN_ORG = "SELECT h.* FROM hero h JOIN " +
                "org_hero o ON o.heroId = h.id WHERE o.orgId = ?";
        List<Hero> heroes = jdbc.query(LIST_HEROES_IN_ORG, new HeroMapper(), organization.getId());
        for(Hero h : heroes) {
            h.setSuperPower(getPowerForHero(h.getId()));
        }
        return heroes;
    }

    @Override
    public List<Hero> listHeroesByLocation(Location location) {
        final String LIST_HEROES_IN_LOC = "SELECT h.* FROM hero h JOIN " +
                "sighting s ON s.heroId = h.id WHERE s.locId = ?";
        List<Hero> heroes = jdbc.query(LIST_HEROES_IN_LOC, new HeroMapper(), location.getId());
        for(Hero h : heroes) {
            h.setSuperPower(getPowerForHero(h.getId()));
        }
        return heroes;
    }

    private SuperPower getPowerForHero(int id) {
        final String SELECT_POWER_FOR_HERO = "SELECT s.* FROM superPower s JOIN "+
                "hero h ON h.superPowerId = s.id WHERE h.id = ?";
        return jdbc.queryForObject(SELECT_POWER_FOR_HERO, new SuperPowerDaoDB.superPowerMapper(), id);
    }

    public static final class HeroMapper implements RowMapper<Hero> {
        @Override
        public Hero mapRow(ResultSet resultSet, int i) throws SQLException {
            Hero hero = new Hero();
            hero.setId(resultSet.getInt("id"));
            hero.setName(resultSet.getString("heroName"));
            hero.setDescription(resultSet.getString("heroDescription"));
            return hero;
        }
    }
}