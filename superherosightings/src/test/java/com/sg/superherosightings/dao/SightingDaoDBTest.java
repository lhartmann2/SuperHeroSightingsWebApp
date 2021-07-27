package com.sg.superherosightings.dao;

import com.sg.superherosightings.entity.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SightingDaoDBTest {

    @Autowired
    HeroDao heroDao;

    @Autowired
    LocationDao locationDao;

    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    SightingDao sightingDao;

    @Autowired
    SuperPowerDao superPowerDao;

    @Before
    public void setUp() {

        List<Sighting> sightings = sightingDao.getAllSightings();
        for(Sighting s : sightings) {
            sightingDao.deleteSightingById(s.getId());
        }

        List<Location> locations = locationDao.getAllLocations();
        for(Location l : locations) {
            locationDao.deleteLocationById(l.getId());
        }

        List<Organization> organizations = organizationDao.getAllOrganizations();
        for(Organization o : organizations) {
            organizationDao.deleteOrganizationById(o.getId());
        }

        List<Hero> heroes = heroDao.getAllHeroes();
        for(Hero h : heroes) {
            heroDao.deleteHeroById(h.getId());
        }

        List<SuperPower> powers = superPowerDao.getAllSuperPowers();
        for(SuperPower s : powers) {
            superPowerDao.deleteSuperPowerById(s.getId());
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddAndGetSightingByHero() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("red");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        Location location = new Location();
        location.setName("a place");
        location.setDescription("somewhere");
        location.setAddress("5 main st");
        location.setLatitude("44N");
        location.setLongitude("5W");
        locationDao.addLocation(location);

        Sighting sighting = new Sighting();
        sighting.setHeroId(hero.getId());
        sighting.setLocationId(location.getId());
        sighting.setSightingDate(new Timestamp(System.currentTimeMillis()));
        sightingDao.addSighting(sighting);

        List<Sighting> fromDao = sightingDao.getSightingByHero(hero);

        assertEquals(1, fromDao.size());

        for(Sighting s : fromDao) {
            assertEquals(s, sighting);
        }
    }

    @Test
    public void testAddSightingAndGetByLocation() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("red");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        Location location = new Location();
        location.setName("a place");
        location.setDescription("somewhere");
        location.setAddress("5 main st");
        location.setLatitude("44N");
        location.setLongitude("5W");
        locationDao.addLocation(location);

        Sighting sighting = new Sighting();
        sighting.setHeroId(hero.getId());
        sighting.setLocationId(location.getId());
        sighting.setSightingDate(new Timestamp(System.currentTimeMillis()));
        sightingDao.addSighting(sighting);

        List<Sighting> fromDao = sightingDao.getSightingByLocation(location);

        assertEquals(1, fromDao.size());

        for(Sighting s : fromDao) {
            assertEquals(s, sighting);
        }
    }

    @Test
    public void testAddSightingAndGetByDate() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("red");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        Location location = new Location();
        location.setName("a place");
        location.setDescription("somewhere");
        location.setAddress("5 main st");
        location.setLatitude("44N");
        location.setLongitude("5W");
        locationDao.addLocation(location);

        Sighting sighting = new Sighting();
        sighting.setHeroId(hero.getId());
        sighting.setLocationId(location.getId());
        Timestamp date = new Timestamp(System.currentTimeMillis());
        sighting.setSightingDate(date);
        sightingDao.addSighting(sighting);

        List<Sighting> fromDao = sightingDao.getSightingByDate(date);

        assertEquals(1, fromDao.size());

        for(Sighting s : fromDao) {
            assertEquals(s, sighting);
        }
    }

    @Test
    public void testGetAllSightings() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("red");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        Hero hero2 = new Hero();
        hero2.setName("Luigi");
        hero2.setDescription("green");
        hero2.setSuperPower(power);
        heroDao.addHero(hero2);

        Location location = new Location();
        location.setName("a place");
        location.setDescription("somewhere");
        location.setAddress("5 main st");
        location.setLatitude("44N");
        location.setLongitude("5W");
        locationDao.addLocation(location);

        Sighting sighting = new Sighting();
        sighting.setHeroId(hero.getId());
        sighting.setLocationId(location.getId());
        sighting.setSightingDate(new Timestamp(System.currentTimeMillis()));
        sightingDao.addSighting(sighting);

        Sighting sighting2 = new Sighting();
        sighting2.setHeroId(hero2.getId());
        sighting2.setLocationId(location.getId());
        sighting2.setSightingDate(new Timestamp(System.currentTimeMillis()-1));
        sightingDao.addSighting(sighting2);


        List<Sighting> fromDao = sightingDao.getAllSightings();

        assertEquals(2, fromDao.size());
        assertTrue(fromDao.contains(sighting));
        assertTrue(fromDao.contains(sighting2));
    }

    @Test
    public void testDeleteSightingById() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("red");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        Hero hero2 = new Hero();
        hero2.setName("Luigi");
        hero2.setDescription("green");
        hero2.setSuperPower(power);
        heroDao.addHero(hero2);

        Location location = new Location();
        location.setName("a place");
        location.setDescription("somewhere");
        location.setAddress("5 main st");
        location.setLatitude("44N");
        location.setLongitude("5W");
        locationDao.addLocation(location);

        Sighting sighting = new Sighting();
        sighting.setHeroId(hero.getId());
        sighting.setLocationId(location.getId());
        sighting.setSightingDate(new Timestamp(System.currentTimeMillis()));
        sighting = sightingDao.addSighting(sighting);

        Sighting fromDao = sightingDao.getSightingById(sighting.getId());
        assertEquals(sighting, fromDao);

        sightingDao.deleteSightingById(fromDao.getId());
        fromDao = sightingDao.getSightingById(sighting.getId());
        assertNull(fromDao);
    }

    @Test
    public void testFailAddSighting() {
        Sighting sighting = new Sighting();
        //sighting.setHeroId(hero.getId()); Null NOT NULL field
        //sighting.setLocationId(location.getId()); Null NOT NULL field
        Timestamp date1 = new Timestamp(System.currentTimeMillis());
        sighting.setSightingDate(date1);

        assertThrows(DataIntegrityViolationException.class, () ->
                sightingDao.addSighting(sighting));
    }

    @Test
    public void testFailGetSightingByHero() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("red");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        Location location = new Location();
        location.setName("a place");
        location.setDescription("somewhere");
        location.setAddress("5 main st");
        location.setLatitude("44N");
        location.setLongitude("5W");
        locationDao.addLocation(location);

        Sighting sighting = new Sighting();
        sighting.setHeroId(hero.getId());
        sighting.setLocationId(location.getId());
        Timestamp date1 = new Timestamp(System.currentTimeMillis());
        sighting.setSightingDate(date1);
        sightingDao.addSighting(sighting);

        Hero badHero = new Hero(); //Dummy hero

        List<Sighting> list = sightingDao.getSightingByHero(badHero); //Shouldn't care
        assertTrue(list.isEmpty());
    }

    @Test
    public void testFailGetSightingByLocation() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("red");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        Location location = new Location();
        location.setName("a place");
        location.setDescription("somewhere");
        location.setAddress("5 main st");
        location.setLatitude("44N");
        location.setLongitude("5W");
        locationDao.addLocation(location);

        Sighting sighting = new Sighting();
        sighting.setHeroId(hero.getId());
        sighting.setLocationId(location.getId());
        Timestamp date1 = new Timestamp(System.currentTimeMillis());
        sighting.setSightingDate(date1);
        sightingDao.addSighting(sighting);

        Location badLocation = new Location(); //Dummy location

        List<Sighting> list = sightingDao.getSightingByLocation(badLocation); //Shouldn't care
        assertTrue(list.isEmpty());
    }

    @Test
    public void testFailGetSightingByDate() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("red");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        Location location = new Location();
        location.setName("a place");
        location.setDescription("somewhere");
        location.setAddress("5 main st");
        location.setLatitude("44N");
        location.setLongitude("5W");
        locationDao.addLocation(location);

        Sighting sighting = new Sighting();
        sighting.setHeroId(hero.getId());
        sighting.setLocationId(location.getId());
        Timestamp date1 = new Timestamp(System.currentTimeMillis());
        sighting.setSightingDate(date1);
        sightingDao.addSighting(sighting);

        Timestamp badTime = new Timestamp(-1);

        List<Sighting> list = sightingDao.getSightingByDate(badTime); //Shouldn't care
        assertTrue(list.isEmpty());
    }

    @Test
    public void testFailDeleteSightingByHero() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("red");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        Location location = new Location();
        location.setName("a place");
        location.setDescription("somewhere");
        location.setAddress("5 main st");
        location.setLatitude("44N");
        location.setLongitude("5W");
        locationDao.addLocation(location);

        Sighting sighting = new Sighting();
        sighting.setHeroId(hero.getId());
        sighting.setLocationId(location.getId());
        Timestamp date1 = new Timestamp(System.currentTimeMillis());
        sighting.setSightingDate(date1);
        sightingDao.addSighting(sighting);

        Hero badHero = new Hero();

        //sightingDao.deleteSightingByHero(badHero);
        assertTrue(sightingDao.getSightingByHero(badHero).isEmpty());
    }

    @Test
    public void testFailDeleteSightingByLocation() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("red");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        Location location = new Location();
        location.setName("a place");
        location.setDescription("somewhere");
        location.setAddress("5 main st");
        location.setLatitude("44N");
        location.setLongitude("5W");
        locationDao.addLocation(location);

        Sighting sighting = new Sighting();
        sighting.setHeroId(hero.getId());
        sighting.setLocationId(location.getId());
        Timestamp date1 = new Timestamp(System.currentTimeMillis());
        sighting.setSightingDate(date1);
        sightingDao.addSighting(sighting);

        Location badLoc = new Location(); //dummy location

        //sightingDao.deleteSightingByLocation(badLoc);
        assertTrue(sightingDao.getSightingByLocation(badLoc).isEmpty());
    }

    @Test
    public void testFailDeleteSightingByDate() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("red");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        Location location = new Location();
        location.setName("a place");
        location.setDescription("somewhere");
        location.setAddress("5 main st");
        location.setLatitude("44N");
        location.setLongitude("5W");
        locationDao.addLocation(location);

        Sighting sighting = new Sighting();
        sighting.setHeroId(hero.getId());
        sighting.setLocationId(location.getId());
        Timestamp date1 = new Timestamp(System.currentTimeMillis());
        sighting.setSightingDate(date1);
        sightingDao.addSighting(sighting);

        Timestamp badTime = new Timestamp(-1);

        //sightingDao.deleteSightingByDate(badTime);
        assertTrue(sightingDao.getSightingByDate(badTime).isEmpty());
    }

}