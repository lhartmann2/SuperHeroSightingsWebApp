package com.sg.superherosightings.dao;

import com.sg.superherosightings.entity.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
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
public class HeroDaoDBTest {

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
    public void testAddAndGetHero() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("He's super");
        hero.setSuperPower(power);

        heroDao.addHero(hero);

        Hero fromDao = heroDao.getHeroById(hero.getId());
        assertEquals(hero, fromDao);
    }

    @Test
    public void testGetAllHeroes() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("He's super");
        hero.setSuperPower(power);

        Hero hero2 = new Hero();
        hero2.setName("Luigi");
        hero2.setDescription("Not as super");
        hero2.setSuperPower(power);

        heroDao.addHero(hero);
        heroDao.addHero(hero2);

        List<Hero> heroes = heroDao.getAllHeroes();

        assertEquals(2, heroes.size());
        assertTrue(heroes.contains(hero));
        assertTrue(heroes.contains(hero2));
    }

    @Test
    public void testUpdateHero() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("He's super");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        Hero fromDao = heroDao.getHeroById(hero.getId());
        assertEquals(hero, fromDao);

        hero.setName("different name");
        heroDao.updateHero(hero);

        assertNotEquals(hero, fromDao);

        fromDao = heroDao.getHeroById(hero.getId());
        assertEquals(hero, fromDao);
    }

    @Test
    public void testDeleteHero() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("He's super");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        Hero fromDao = heroDao.getHeroById(hero.getId());
        assertEquals(hero, fromDao);

        heroDao.deleteHeroById(hero.getId());
        fromDao = heroDao.getHeroById(hero.getId());
        assertNull(fromDao);
    }

    @Test
    public void testListHeroesInOrg() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("He's super");
        hero.setSuperPower(power);

        Hero hero1 = new Hero();
        hero1.setName("Luigi");
        hero1.setDescription("Green");
        hero1.setSuperPower(power);

        Hero hero2 = new Hero();
        hero2.setName("Sonic");
        hero2.setDescription("blue");
        hero2.setSuperPower(power);

        heroDao.addHero(hero);
        heroDao.addHero(hero1);
        heroDao.addHero(hero2);

        Organization organization = new Organization();
        organization.setName("Washed up heroes");
        organization.setDescription("Remember the 80s?");
        organization.setAddress("4 Main St");
        organization.setEmail("info@sega.com");
        organizationDao.addOrganization(organization);
        for(Hero h : heroDao.getAllHeroes()) {
            organizationDao.addHeroToOrg(organization, h);
        }

        List<Hero> heroesInOrg = heroDao.listHeroesInOrg(organization);
        assertEquals(3, heroesInOrg.size());
        assertTrue(heroesInOrg.contains(hero));
        assertTrue(heroesInOrg.contains(hero1));
        assertTrue(heroesInOrg.contains(hero2));
    }

    @Test
    public void testListHeroesInLocation() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("He's super");
        hero.setSuperPower(power);

        Hero hero1 = new Hero();
        hero1.setName("Luigi");
        hero1.setDescription("Green");
        hero1.setSuperPower(power);

        Hero hero2 = new Hero();
        hero2.setName("Sonic");
        hero2.setDescription("blue");
        hero2.setSuperPower(power);

        heroDao.addHero(hero);
        heroDao.addHero(hero1);
        heroDao.addHero(hero2);

        Location location = new Location();
        location.setName("Twin Peaks");
        location.setDescription("The owls are not what they seem.");
        location.setAddress("4 Main st");
        location.setLatitude("44W");
        location.setLongitude("-5N");
        locationDao.addLocation(location);

        Sighting sighting1 = new Sighting();
        sighting1.setHeroId(hero.getId());
        sighting1.setLocationId(location.getId());
        sighting1.setSightingDate(new Timestamp(System.currentTimeMillis()));

        Sighting sighting2 = new Sighting();
        sighting2.setHeroId(hero1.getId());
        sighting2.setLocationId(location.getId());
        sighting2.setSightingDate(new Timestamp(System.currentTimeMillis()-1));

        Sighting sighting3 = new Sighting();
        sighting3.setHeroId(hero2.getId());
        sighting3.setLocationId(location.getId());
        sighting3.setSightingDate(new Timestamp(System.currentTimeMillis()-2));

        sightingDao.addSighting(sighting1);
        sightingDao.addSighting(sighting2);
        sightingDao.addSighting(sighting3);

        List<Hero> heroesByLoc = heroDao.listHeroesByLocation(location);

        assertEquals(3, heroesByLoc.size());
        assertTrue(heroesByLoc.contains(hero));
        assertTrue(heroesByLoc.contains(hero1));
        assertTrue(heroesByLoc.contains(hero2));
    }

    @Test
    public void testFailAddHero() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        //hero.setName("Mario"); //No name for NOT NULL field
        hero.setDescription("He's super");
        hero.setSuperPower(power);

        assertThrows(DataIntegrityViolationException.class, () ->
                heroDao.addHero(hero));
    }

    @Test
    public void testFailUpdateHero() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("He's super");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        assertNotNull(heroDao.getHeroById(hero.getId()));

        Hero badHero = new Hero();
        badHero.setId(hero.getId());
        badHero.setName("Mario");
        //Null description
        badHero.setSuperPower(power);

        assertThrows(DataIntegrityViolationException.class, () ->
                heroDao.updateHero(badHero));

    }

    @Test
    public void testFailGetHeroById() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("He's super");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        Hero fromDao = heroDao.getHeroById(hero.getId());
        assertNotNull(fromDao);

        fromDao = heroDao.getHeroById(500);
        assertNull(fromDao);
    }

    @Test
    public void testFailDeleteHeroById() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("He's super");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        heroDao.deleteHeroById(hero.getId());
        assertNull(heroDao.getHeroById(hero.getId()));
        heroDao.deleteHeroById(hero.getId()); //Shouldn't care?
        assertNull(heroDao.getHeroById(hero.getId()));
    }

    @Test
    public void testFailGetAllHeroesInOrgAndLoc() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Hero hero = new Hero();
        hero.setName("Mario");
        hero.setDescription("He's super");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        Organization organization = new Organization(); //Dummy org
        Location location = new Location(); //Dummy location

        List<Hero> list = heroDao.listHeroesInOrg(organization); //Empty list
        assertTrue(list.isEmpty());

        list = heroDao.listHeroesByLocation(location); //empty list
        assertTrue(list.isEmpty());
    }

}