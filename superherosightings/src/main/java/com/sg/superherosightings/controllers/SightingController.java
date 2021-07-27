package com.sg.superherosightings.controllers;

import com.sg.superherosightings.dao.HeroDao;
import com.sg.superherosightings.dao.LocationDao;
import com.sg.superherosightings.dao.OrganizationDao;
import com.sg.superherosightings.dao.SightingDao;
import com.sg.superherosightings.entity.Hero;
import com.sg.superherosightings.entity.Location;
import com.sg.superherosightings.entity.Organization;
import com.sg.superherosightings.entity.Sighting;
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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class SightingController {

    @Autowired
    SightingDao sightingDao;

    @Autowired
    HeroDao heroDao;

    @Autowired
    LocationDao locationDao;

    @Autowired
    OrganizationDao organizationDao;

    private String dateError;
    private Set<ConstraintViolation<Sighting>> violations;

    public SightingController() {
        dateError = new String();
        violations = new HashSet<>();
    }

    @GetMapping("index") //Home / Landing Page
    public String indexPage(Model model) {
        List<Sighting> sightings = sightingDao.getTenRecentSightings();
        model.addAttribute("sightings", sightings);
        return "index";
    }

    @GetMapping("") //Home / Landing Page
    public String landingPage(Model model) {
        List<Sighting> sightings = sightingDao.getTenRecentSightings();
        model.addAttribute("sightings", sightings);
        return "index";
    }

    @GetMapping("sightings")
    public String displaySightings(Model model) {
        List<Hero> heroes = heroDao.getAllHeroes();
        List<Location> locations = locationDao.getAllLocations();
        List<Sighting> sightings = sightingDao.getAllSightings();

        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);
        model.addAttribute("sightings", sightings);
        model.addAttribute("errors", violations);
        model.addAttribute("dateError", dateError);
        dateError = new String();

        return "sightings";
    }

    @GetMapping("deleteSighting")
    public String deleteSighting(Integer id, Model model) {
        Sighting sighting = sightingDao.getSightingById(id);
        List<Organization> orgs = organizationDao.getAllOrgsOfHero(heroDao.getHeroById(sighting.getHeroId()));
        model.addAttribute("sighting", sighting);
        model.addAttribute("orgs", orgs);
        return "deleteSighting";
    }

    @GetMapping("performDeleteSighting")
    public String performDeleteSighting(int id) {
        sightingDao.deleteSightingById(id);
        return "redirect:/sightings";
    }

    @GetMapping("editSighting")
    public String editSighting(Integer id, Model model) {
        Sighting sighting = sightingDao.getSightingById(id);
        List<Hero> heroes = heroDao.getAllHeroes();
        List<Location> locations = locationDao.getAllLocations();

        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);
        model.addAttribute("sighting", sighting);
        model.addAttribute("errors", violations);

        return "editSighting";
    }

    @GetMapping("sightingDetail")
    public String sightingDetail(Integer id, Model model) {
        Sighting sighting = sightingDao.getSightingById(id);
        Hero hero = heroDao.getHeroById(sighting.getHeroId());
        Location location = locationDao.getLocationById(sighting.getLocationId());
        List<Organization> orgs = organizationDao.getAllOrgsOfHero(heroDao.getHeroById(sighting.getHeroId()));
        model.addAttribute("sighting", sighting);
        model.addAttribute("hero", hero);
        model.addAttribute("location", location);
        model.addAttribute("orgs", orgs);
        return "sightingDetail";
    }

    @GetMapping("sightingsByHero")
    public String sightingsByHero(Integer id, Model model) {
        List<Sighting> sightings = sightingDao.getSightingByHero(heroDao.getHeroById(id));
        List<Hero> heroes = heroDao.getAllHeroes();
        List<Location> locations = locationDao.getAllLocations();
        Hero searchHero = heroDao.getHeroById(id);
        model.addAttribute("sightings", sightings);
        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);
        model.addAttribute("searchHero", searchHero);
        return "sightingsByHero";
    }

    @GetMapping("sightingsByLocation")
    public String sightingsByLocation(Integer id, Model model) {
        List<Sighting> sightings = sightingDao.getSightingByLocation(locationDao.getLocationById(id));
        List<Hero> heroes = heroDao.getAllHeroes();
        List<Location> locations = locationDao.getAllLocations();
        Location searchLocation = locationDao.getLocationById(id);
        model.addAttribute("sightings", sightings);
        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);
        model.addAttribute("searchLocation", searchLocation);
        return "sightingsByLocation";
    }

    @GetMapping("sightingsByDate")
    public String sightingsByDate(HttpServletRequest request, Model model) {
        String dateStr = request.getParameter("dateStr");
        String dt = dateStr.concat(" ").concat("00:00:00.0");
        List<Sighting> sightings = sightingDao.getSightingByDate(Timestamp.valueOf(dt));
        List<Hero> heroes = heroDao.getAllHeroes();
        List<Location> locations = locationDao.getAllLocations();
        model.addAttribute("sightings", sightings);
        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);
        model.addAttribute("dateStr", dateStr);
        return "sightingsByDate";
    }

    @PostMapping("addSighting")
    public String addSighting(HttpServletRequest request) {
        int heroId = Integer.parseInt(request.getParameter("heroId"));
        int locationId = Integer.parseInt(request.getParameter("locationId"));
        String dateStr = request.getParameter("sightingDateStr");
        String dt = dateStr.concat(" ").concat("00:00:00.0");
        Timestamp ts = Timestamp.valueOf(dt);

        Sighting sighting = new Sighting();
        sighting.setHeroId(heroId);
        sighting.setLocationId(locationId);
        sighting.setSightingDate(ts);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validator.validate(sighting);

        if(violations.isEmpty()) {
            sightingDao.addSighting(sighting);
        }

        return "redirect:/sightings";
    }

    @PostMapping("editSighting")
    public String performEditSighting(HttpServletRequest request) {
        int heroId = Integer.parseInt(request.getParameter("heroId"));
        int locationId = Integer.parseInt(request.getParameter("locationId"));
        int id = Integer.parseInt(request.getParameter("id"));
        String dateStr = request.getParameter("sightingDateStr");
        String dt = dateStr.concat(" ").concat("00:00:00.0");
        Timestamp ts = Timestamp.valueOf(dt);

        Sighting sighting = new Sighting();
        sighting.setId(id);
        sighting.setHeroId(heroId);
        sighting.setLocationId(locationId);
        sighting.setSightingDate(ts);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validator.validate(sighting);

        if(violations.isEmpty()) {
            sightingDao.updateSighting(sighting);
            return "redirect:/sightings";
        } else {
            return "editSighting";
        }
    }
}
