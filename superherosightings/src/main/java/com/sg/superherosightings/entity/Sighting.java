package com.sg.superherosightings.entity;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sighting {

    private int id;

    @NotNull(message = "Sighting must have a hero.")
    private int heroId;

    private Hero hero;

    @NotNull(message = "Sighting must have a location.")
    private int locationId;

    private Location location;

    @NotNull(message = "Sighting must have a timestamp.")
    private Timestamp sightingDate;

    public int getHeroId() {
        return heroId;
    }

    public void setHeroId(int heroId) {
        this.heroId = heroId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public Timestamp getSightingDate() {
        return sightingDate;
    }

    public void setSightingDate(Timestamp timestamp) {
        this.sightingDate = timestamp;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sighting sighting = (Sighting) o;

        if (getHeroId() != sighting.getHeroId()) return false;
        if (getLocationId() != sighting.getLocationId()) return false;
        return getSightingDate().equals(sighting.getSightingDate());
    }

    @Override
    public int hashCode() {
        int result = getHeroId();
        result = 31 * result + getLocationId();
        result = 31 * result + getSightingDate().hashCode();
        return result;
    }
}
