package com.sg.superherosightings.dao;

import com.sg.superherosightings.entity.Hero;
import com.sg.superherosightings.entity.Location;
import com.sg.superherosightings.entity.Organization;

import java.util.List;

public interface HeroDao {

    Hero addHero(Hero hero);
    Hero getHeroById(int id);
    void updateHero(Hero hero);
    void deleteHeroById(int id);
    List<Hero> getAllHeroes();

    List<Hero> listHeroesInOrg(Organization organization);

    List<Hero> listHeroesByLocation(Location location);
}
