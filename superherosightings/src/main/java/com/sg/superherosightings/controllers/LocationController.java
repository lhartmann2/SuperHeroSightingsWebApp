package com.sg.superherosightings.controllers;

import com.sg.superherosightings.dao.HeroDao;
import com.sg.superherosightings.dao.LocationDao;
import com.sg.superherosightings.entity.Hero;
import com.sg.superherosightings.entity.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class LocationController {

    @Autowired
    LocationDao locationDao;

    @Autowired
    HeroDao heroDao;

    private List<FieldError> errors;
    private Set<ConstraintViolation<Location>> violations;

    public LocationController() {
        errors = new ArrayList<>();
        violations = new HashSet<>();
    }

    @GetMapping("locations")
    public String displayLocations(Model model) {
        List<Location> locations = locationDao.getAllLocations();
        List<Hero> heroes = heroDao.getAllHeroes();
        model.addAttribute("locations", locations);
        model.addAttribute("heroes", heroes);
        model.addAttribute("errors", violations);

        return "locations";
    }

    @GetMapping("deleteLocation")
    public String deleteLocation(Integer id, Model model) {
        Location location = locationDao.getLocationById(id);
        model.addAttribute("loc", location);
        return "deleteLocation";
    }

    @GetMapping("performDeleteLocation")
    public String performDeleteLocation(int id) {
        locationDao.deleteLocationById(id);
        return "redirect:/locations";
    }

    @GetMapping("editLocation")
    public String editLocation(Integer id, Model model) {
        Location location = locationDao.getLocationById(id);
        model.addAttribute("location", location);
        return "editLocation";
    }

    @GetMapping("locationDetail")
    public String locationDetails(Integer id, Model model) {
        Location location = locationDao.getLocationById(id);
        model.addAttribute("location", location);
        return "locationDetail";
    }

    @GetMapping("locationsByHero")
    public String displayLocationsByHero(Integer id, Model model) {
        Hero hero = heroDao.getHeroById(id);
        List<Location> locations = locationDao.getAllLocationsOfHero(hero);
        List<Hero> heroes = heroDao.getAllHeroes();
        model.addAttribute("hero", hero);
        model.addAttribute("locations", locations);
        model.addAttribute("heroes", heroes);
        return "locationsByHero";
    }

    @PostMapping("addLocation")
    public String addLocation(HttpServletRequest request) {
        Location newLocation = new Location();
        newLocation.setName(request.getParameter("name"));
        newLocation.setDescription(request.getParameter("description"));
        newLocation.setAddress(request.getParameter("address"));
        newLocation.setLatitude(request.getParameter("latitude"));
        newLocation.setLongitude(request.getParameter("longitude"));

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validator.validate(newLocation);

        if(violations.isEmpty()) {
            locationDao.addLocation(newLocation);
        }
        return "redirect:/locations";
    }

    @PostMapping("editLocation")
    public String performEditLocation(@Valid Location location, BindingResult result) {
        if(result.hasErrors()) {
            errors = result.getFieldErrors();
            return "editLocation";
        } else {
            locationDao.updateLocation(location);
            return "redirect:/locations";
        }
    }
}
