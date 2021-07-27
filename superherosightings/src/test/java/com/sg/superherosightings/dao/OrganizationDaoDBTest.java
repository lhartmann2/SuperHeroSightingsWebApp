package com.sg.superherosightings.dao;

import com.sg.superherosightings.entity.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.crypto.Data;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganizationDaoDBTest {

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
    public void testAddAndGetOrganization() {
        Organization organization = new Organization();
        organization.setName("club");
        organization.setDescription("super exclusive");
        organization.setAddress("44 secret place");
        organization.setEmail("club@secret.biz");
        organizationDao.addOrganization(organization);

        Organization fromDao = organizationDao.getOrganizationById(organization.getId());

        assertEquals(organization, fromDao);
    }

    @Test
    public void testUpdateOrganization() {
        Organization organization = new Organization();
        organization.setName("club");
        organization.setDescription("super exclusive");
        organization.setAddress("44 secret place");
        organization.setEmail("club@secret.biz");
        organizationDao.addOrganization(organization);

        Organization fromDao = organizationDao.getOrganizationById(organization.getId());

        organization.setName("boring club");
        organizationDao.updateOrganization(organization);
        assertNotEquals(organization, fromDao);

        fromDao = organizationDao.getOrganizationById(organization.getId());
        assertEquals(organization, fromDao);
    }

    @Test
    public void testGetAllOrganizations() {
        Organization organization = new Organization();
        organization.setName("club");
        organization.setDescription("super exclusive");
        organization.setAddress("44 secret place");
        organization.setEmail("club@secret.biz");

        Organization organization1 = new Organization();
        organization1.setName("org2");
        organization1.setDescription("another org");
        organization1.setAddress("23 west road");
        organization1.setEmail("info@lhpgames.com");

        organizationDao.addOrganization(organization);
        organizationDao.addOrganization(organization1);

        List<Organization> organizations = organizationDao.getAllOrganizations();

        assertEquals(2, organizations.size());
        assertTrue(organizations.contains(organization));
        assertTrue(organizations.contains(organization1));
    }

    @Test
    public void testDeleteOrganization() {
        Organization organization = new Organization();
        organization.setName("club");
        organization.setDescription("super exclusive");
        organization.setAddress("44 secret place");
        organization.setEmail("club@secret.biz");
        organizationDao.addOrganization(organization);

        Organization fromDao = organizationDao.getOrganizationById(organization.getId());

        assertEquals(organization, fromDao);

        organizationDao.deleteOrganizationById(organization.getId());
        fromDao = organizationDao.getOrganizationById(organization.getId());
        assertNull(fromDao);
    }

    @Test
    public void testAddHeroToOrgAndListHeroesInOrg() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Organization organization = new Organization();
        organization.setName("club");
        organization.setDescription("super exclusive");
        organization.setAddress("44 secret place");
        organization.setEmail("club@secret.biz");
        organizationDao.addOrganization(organization);

        Hero hero = new Hero();
        hero.setName("mario");
        hero.setDescription("he's red");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        organization = organizationDao.addHeroToOrg(organization, hero);

        List<Organization> orgs = organizationDao.getAllOrgsOfHero(hero);

        assertEquals(1, orgs.size());
        assertTrue(orgs.contains(organization));
    }

    @Test
    public void testFailAddOrg() {
        Organization organization = new Organization();
        //organization.setName("club"); //Null NOT NULL field
        organization.setDescription("super exclusive");
        organization.setAddress("44 secret place");
        organization.setEmail("club@secret.biz");

        assertThrows(DataIntegrityViolationException.class, () ->
                organizationDao.addOrganization(organization));
    }

    @Test
    public void testFailGetOrgById() {
        Organization organization = new Organization();
        organization.setName("club");
        organization.setDescription("super exclusive");
        organization.setAddress("44 secret place");
        organization.setEmail("club@secret.biz");
        organizationDao.addOrganization(organization);

        Organization fromDao = organizationDao.getOrganizationById(organization.getId());
        assertNotNull(fromDao);
        fromDao = organizationDao.getOrganizationById(500);
        assertNull(fromDao);
    }

    @Test
    public void testFailUpdateOrg() {
        Organization organization = new Organization();
        organization.setName("club");
        organization.setDescription("super exclusive");
        organization.setAddress("44 secret place");
        organization.setEmail("club@secret.biz");
        organizationDao.addOrganization(organization);

        assertNotNull(organizationDao.getOrganizationById(organization.getId()));

        Organization badOrg = new Organization();
        badOrg.setId(organization.getId());
        //null name
        organization.setDescription("super exclusive");
        organization.setAddress("44 secret place");
        organization.setEmail("club@secret.biz");

        assertThrows(DataIntegrityViolationException.class, () ->
                organizationDao.updateOrganization(badOrg));
    }

    @Test
    public void testFailDeleteOrgById() {
        Organization organization = new Organization();
        organization.setName("club");
        organization.setDescription("super exclusive");
        organization.setAddress("44 secret place");
        organization.setEmail("club@secret.biz");
        organizationDao.addOrganization(organization);

        organizationDao.deleteOrganizationById(organization.getId());
        assertNull(organizationDao.getOrganizationById(organization.getId()));
        organizationDao.deleteOrganizationById(organization.getId());
        assertNull(organizationDao.getOrganizationById(organization.getId()));
    }

    @Test
    public void testFailAddHeroToOrg() {
        SuperPower power = new SuperPower();
        power.setSuperPowerName("jumping");
        superPowerDao.addSuperPower(power);

        Organization organization = new Organization();
        organization.setName("club");
        organization.setDescription("super exclusive");
        organization.setAddress("44 secret place");
        organization.setEmail("club@secret.biz");
        organizationDao.addOrganization(organization);

        Hero badHero = new Hero(); //Dummy hero

        assertThrows(DataIntegrityViolationException.class, () ->
                organizationDao.addHeroToOrg(organization, badHero));

        Organization badOrg = new Organization(); //Dummy org

        Hero hero = new Hero(); //Valid hero
        hero.setName("mario");
        hero.setDescription("red");
        hero.setSuperPower(power);
        heroDao.addHero(hero);

        assertThrows(DataIntegrityViolationException.class, () ->
                organizationDao.addHeroToOrg(badOrg, hero));
    }

    @Test
    public void testFailClearMembers() {
        Organization badOrg = new Organization(); //dummy org

        assertThrows(NullPointerException.class, () ->
                organizationDao.clearMembers(badOrg));

        Organization organization = new Organization();
        organization.setName("club");
        organization.setDescription("super exclusive");
        organization.setAddress("44 secret place");
        organization.setEmail("club@secret.biz");
        organizationDao.addOrganization(organization);

        //Shouldn't care when calling on properly formed org
        organizationDao.clearMembers(organization);
        assertTrue(heroDao.listHeroesInOrg(organization).isEmpty());
    }

    @Test
    public void testFailGetAllOrgsOfHero() {
        Organization organization = new Organization();
        organization.setName("club");
        organization.setDescription("super exclusive");
        organization.setAddress("44 secret place");
        organization.setEmail("club@secret.biz");
        organizationDao.addOrganization(organization);

        Hero badHero = new Hero(); //Dummy hero

        List<Organization> list = organizationDao.getAllOrgsOfHero(badHero);
        assertTrue(list.isEmpty());
    }

}