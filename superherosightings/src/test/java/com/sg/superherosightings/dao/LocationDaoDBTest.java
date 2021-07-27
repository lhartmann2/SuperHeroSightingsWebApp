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

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationDaoDBTest {

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
    public void testAddAndGetLocation() {
        Location location = new Location();
        location.setName("Somewhere");
        location.setDescription("someplace");
        location.setAddress("6 road street");
        location.setLatitude("5N");
        location.setLongitude("6W");
        locationDao.addLocation(location);

        Location fromDao = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao);
    }

    @Test
    public void testGetAllLocations() {
        Location location = new Location();
        location.setName("Somewhere");
        location.setDescription("someplace");
        location.setAddress("6 road street");
        location.setLatitude("5N");
        location.setLongitude("6W");

        Location location1 = new Location();
        location1.setName("another place");
        location1.setDescription("someplace else");
        location1.setAddress("5 street road");
        location1.setLatitude("-22N");
        location1.setLongitude("64W");

        locationDao.addLocation(location);
        locationDao.addLocation(location1);

        List<Location> locations = locationDao.getAllLocations();

        assertEquals(2, locations.size());
        assertTrue(locations.contains(location));
        assertTrue(locations.contains(location1));
    }

    @Test
    public void testUpdateLocation() {
        Location location = new Location();
        location.setName("Somewhere");
        location.setDescription("someplace");
        location.setAddress("6 road street");
        location.setLatitude("5N");
        location.setLongitude("6W");
        locationDao.addLocation(location);

        Location fromDao = locationDao.getLocationById(location.getId());

        location.setName("A different name");
        locationDao.updateLocation(location);
        assertNotEquals(location, fromDao);
        fromDao = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao);
    }

    @Test
    public void testDeleteLocation() {
        Location location = new Location();
        location.setName("Somewhere");
        location.setDescription("someplace");
        location.setAddress("6 road street");
        location.setLatitude("5N");
        location.setLongitude("6W");
        locationDao.addLocation(location);

        Location fromDao = locationDao.getLocationById(location.getId());
        assertEquals(fromDao, location);

        locationDao.deleteLocationById(location.getId());
        fromDao = locationDao.getLocationById(location.getId());
        assertNull(fromDao);
    }

    @Test
    public void testGetAllLocationsOfHero() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("He's super");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        Location location = new Location();
        location.setName("Somewhere");
        location.setDescription("someplace");
        location.setAddress("6 road street");
        location.setLatitude("5N");
        location.setLongitude("6W");

        Location location1 = new Location();
        location1.setName("another place");
        location1.setDescription("someplace else");
        location1.setAddress("5 street road");
        location1.setLatitude("-22N");
        location1.setLongitude("64W");

        Location location2 = new Location();
        location2.setName("nowhere");
        location2.setDescription("someplace hidden");
        location2.setAddress("4 hidden road");
        location2.setLatitude("4034N");
        location2.setLongitude("44W");

        locationDao.addLocation(location);
        locationDao.addLocation(location1);
        locationDao.addLocation(location2);

        Sighting sighting1 = new Sighting();
        sighting1.setHeroId(hero.getId());
        sighting1.setLocationId(location.getId());
        sighting1.setSightingDate(new Timestamp(System.currentTimeMillis()));

        Sighting sighting2 = new Sighting();
        sighting2.setHeroId(hero.getId());
        sighting2.setLocationId(location1.getId());
        sighting2.setSightingDate(new Timestamp(System.currentTimeMillis()));

        sightingDao.addSighting(sighting1);
        sightingDao.addSighting(sighting2);

        List<Location> locations = locationDao.getAllLocationsOfHero(hero);

        assertEquals(2, locations.size());
        assertTrue(locations.contains(location));
        assertTrue(locations.contains(location1));
    }

    @Test
    public void testFailAddLocation() {
        Location location = new Location();
        //location.setName("Somewhere"); //null NOT NULL field
        location.setDescription("someplace");
        //location.setAddress("6 road street"); //null NOT NULL field
        location.setLatitude("5N");
        location.setLongitude("6W");

        assertThrows(DataIntegrityViolationException.class, () ->
                locationDao.addLocation(location));
    }

    @Test
    public void testFailUpdateLocation() {
        Location location = new Location();
        location.setName("Somewhere");
        location.setDescription("someplace");
        location.setAddress("6 road street");
        location.setLatitude("5N");
        location.setLongitude("6W");
        locationDao.addLocation(location);

        assertNotNull(locationDao.getLocationById(location.getId()));

        Location badLocation = new Location();
        //No name
        badLocation.setDescription("sompeplace");
        badLocation.setAddress("6 road street");
        badLocation.setLatitude("5N");
        badLocation.setLongitude("6W");
        badLocation.setId(location.getId());

        assertThrows(DataIntegrityViolationException.class, () ->
                locationDao.updateLocation(badLocation));
    }

    @Test
    public void testFailGetLocationById() {
        Location location = new Location();
        location.setName("Somewhere");
        location.setDescription("someplace");
        location.setAddress("6 road street");
        location.setLatitude("5N");
        location.setLongitude("6W");
        locationDao.addLocation(location);

        Location fromDao = locationDao.getLocationById(location.getId());
        assertNotNull(fromDao);
        fromDao = locationDao.getLocationById(500);
        assertNull(fromDao);
    }

    @Test
    public void testFailDeleteLocationById() {
        Location location = new Location();
        location.setName("Somewhere");
        location.setDescription("someplace");
        location.setAddress("6 road street");
        location.setLatitude("5N");
        location.setLongitude("6W");
        locationDao.addLocation(location);

        locationDao.deleteLocationById(location.getId());
        assertNull(locationDao.getLocationById(location.getId()));
        locationDao.deleteLocationById(location.getId());
        assertNull(locationDao.getLocationById(location.getId()));
    }

    @Test
    public void testFailGetAllLocationsOfHero() {
        Location location = new Location();
        location.setName("Somewhere");
        location.setDescription("someplace");
        location.setAddress("6 road street");
        location.setLatitude("5N");
        location.setLongitude("6W");
        locationDao.addLocation(location);

        Hero hero = new Hero(); //Dummy hero

        List<Location> list = locationDao.getAllLocationsOfHero(hero); //empty list
        assertTrue(list.isEmpty());
    }

}