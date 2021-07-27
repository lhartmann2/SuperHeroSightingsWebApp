package com.sg.superherosightings.dao;

import com.sg.superherosightings.entity.Hero;
import com.sg.superherosightings.entity.SuperPower;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SuperPowerDaoDBTest {

    @Autowired
    HeroDao heroDao;

    @Autowired
    SuperPowerDao superPowerDao;

    @Before
    public void setUp() {
        List<Hero> heroList = heroDao.getAllHeroes();
        for(Hero h : heroList) {
            heroDao.deleteHeroById(h.getId());
        }

        List<SuperPower> powers = superPowerDao.getAllSuperPowers();
        for(SuperPower s : powers) {
            superPowerDao.deleteSuperPowerById(s.getId());
        }
    }

    @After
    public void tearDown() {}

    @Test
    public void testAddAndGetSuperPower() {
        SuperPower superPower = new SuperPower();
        superPower.setSuperPowerName("Flight");
        superPowerDao.addSuperPower(superPower);

        SuperPower fromDao = superPowerDao.getSuperPowerById(superPower.getId());
        assertEquals(superPower, fromDao);
    }

    @Test
    public void testUpdateSuperPower() {
        SuperPower superPower = new SuperPower();
        superPower.setSuperPowerName("Flight");
        superPowerDao.addSuperPower(superPower);

        SuperPower fromDao = superPowerDao.getSuperPowerById(superPower.getId());

        superPower.setSuperPowerName("speed");
        superPowerDao.updateSuperPower(superPower);
        assertNotEquals(superPower, fromDao);

        fromDao = superPowerDao.getSuperPowerById(superPower.getId());
        assertEquals(superPower, fromDao);
    }

    @Test
    public void testDeleteSuperPower() {
        SuperPower superPower = new SuperPower();
        superPower.setSuperPowerName("flight");
        superPowerDao.addSuperPower(superPower);

        SuperPower fromDao = superPowerDao.getSuperPowerById(superPower.getId());
        assertEquals(superPower, fromDao);

        superPowerDao.deleteSuperPowerById(superPower.getId());
        fromDao = superPowerDao.getSuperPowerById(superPower.getId());

        assertNull(fromDao);
    }

    @Test
    public void testGetAllSuperPowers() {
        SuperPower power1 = new SuperPower();
        power1.setSuperPowerName("flight");

        SuperPower power2 = new SuperPower();
        power2.setSuperPowerName("invisibility");

        superPowerDao.addSuperPower(power1);
        superPowerDao.addSuperPower(power2);

        List<SuperPower> list = superPowerDao.getAllSuperPowers();

        assertEquals(2, list.size());
        assertTrue(list.contains(power1));
        assertTrue(list.contains(power2));
    }

    @Test
    public void testFailAddPower() {
        SuperPower superPower = new SuperPower();
        //No name / Null

        assertThrows(DataIntegrityViolationException.class, () ->
                superPowerDao.addSuperPower(superPower));
    }

    @Test
    public void testFailUpdatePower() {
        SuperPower superPower = new SuperPower();
        superPower.setSuperPowerName("flight");
        superPowerDao.addSuperPower(superPower);

        SuperPower fromDao = superPowerDao.getSuperPowerById(superPower.getId());

        assertEquals(superPower, fromDao);

        SuperPower badPower = new SuperPower();
        badPower.setId(superPower.getId());
        //No name / null

        assertThrows(DataIntegrityViolationException.class, () ->
                superPowerDao.updateSuperPower(badPower));
    }

}