package com.sg.superherosightings.dao;

import com.sg.superherosightings.entity.Hero;
import com.sg.superherosightings.entity.Location;

import java.util.List;

public interface LocationDao {

    Location addLocation(Location location);
    Location getLocationById(int id);
    void updateLocation(Location location);
    void deleteLocationById(int id);
    List<Location> getAllLocations();

    List<Location> getAllLocationsOfHero(Hero hero);
}
