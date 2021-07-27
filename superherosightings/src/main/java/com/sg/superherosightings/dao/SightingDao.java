package com.sg.superherosightings.dao;

import com.sg.superherosightings.entity.Hero;
import com.sg.superherosightings.entity.Location;
import com.sg.superherosightings.entity.Sighting;
import org.apache.tomcat.jni.Local;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface SightingDao {

    Sighting addSighting(Sighting sighting);
    Sighting getSightingById(int id);
    List<Sighting> getSightingByHero(Hero hero);
    List<Sighting> getSightingByLocation(Location location);
    List<Sighting> getSightingByDate(Date date);

    List<Sighting> getAllSightings();

    List<Sighting> getTenRecentSightings();

    void updateSighting(Sighting sighting);

    void deleteSightingById(int id);
}
