package com.sg.superherosightings.controllers;

import com.sg.superherosightings.dao.HeroDao;
import com.sg.superherosightings.dao.OrganizationDao;
import com.sg.superherosightings.dao.SuperPowerDao;
import com.sg.superherosightings.entity.Hero;
import com.sg.superherosightings.entity.Organization;
import com.sg.superherosightings.entity.SuperPower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.naming.Binding;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class HeroController {

    @Autowired
    HeroDao heroDao;

    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    SuperPowerDao superPowerDao;

    private Set<ConstraintViolation<Hero>> violations;
    private List<FieldError> errors;
    private String orgError, nameError, descError;

    public HeroController() {
        orgError = new String();
        nameError = new String();
        descError = new String();
        violations = new HashSet<>();
        errors = new ArrayList<>();
    }

    @GetMapping("heroes")
    public String displayHeroes(Model model) {
        violations = new HashSet<>();
        List<Hero> heroList = heroDao.getAllHeroes();
        List<SuperPower> powers = superPowerDao.getAllSuperPowers();
        List<Organization> orgs = organizationDao.getAllOrganizations();

        if(powers.isEmpty()) { //Create default superpower if needed
            powers = createDefaultPower();
        }
        if(orgs.isEmpty()) { //Create default organization if needed
            orgs = createDefaultOrg();
        }

        model.addAttribute("powers", powers);
        model.addAttribute("heroes", heroList);
        model.addAttribute("orgs", orgs);
        model.addAttribute("errors", errors);
        model.addAttribute("orgError", orgError);
        model.addAttribute("nameError", nameError);
        model.addAttribute("descError", descError);
        nameError = new String();
        descError = new String();
        orgError = new String();
        return "heroes";
    }

    @GetMapping("deleteHero")
    public String deleteHero(Integer id, Model model) {
        Hero hero = heroDao.getHeroById(id);
        List<Organization> orgs = organizationDao.getAllOrgsOfHero(hero);
        model.addAttribute("hero", hero);
        model.addAttribute("orgs", orgs);
        return "deleteHeroes";
    }

    @GetMapping("performDeleteHero")
    public String performDeleteHero(int id, Model model) {
        heroDao.deleteHeroById(id);
        return "redirect:/heroes";
    }

    @GetMapping("editHeroes")
    public String editHero(Integer id, Model model) {
        Hero hero = heroDao.getHeroById(id);
        List<SuperPower> powers = superPowerDao.getAllSuperPowers();
        List<Organization> orgs = organizationDao.getAllOrganizations();

        if(powers.isEmpty()) { //Create default power if needed.
            powers = createDefaultPower();
        }
        if(orgs.isEmpty()) {
            orgs = createDefaultOrg();
        }

        model.addAttribute("nameError", nameError);
        model.addAttribute("descError", descError);
        model.addAttribute("orgError", orgError);
        model.addAttribute("hero", hero);
        model.addAttribute("powers", powers);
        model.addAttribute("orgs", orgs);
        return "editHeroes";
    }

    @GetMapping("heroDetail")
    public String displayHeroDetail(Integer id, Model model) {
        Hero hero = heroDao.getHeroById(id);
        List<Organization> orgs = organizationDao.getAllOrgsOfHero(hero);
        model.addAttribute("hero", hero);
        model.addAttribute("orgs", orgs);

        return "heroDetail";
    }

    @PostMapping("addHero")
    public String addHero(HttpServletRequest request) {
        String name = request.getParameter("heroName");
        String description = request.getParameter("heroDescription");
        SuperPower power = superPowerDao.getSuperPowerById(Integer.parseInt(request.getParameter("superPower")));
        String[] orgIds = request.getParameterValues("orgId");
        
        boolean errors = false;
        
        if(name.isBlank()) {
            errors=true;
            nameError = "Name must not be blank.";
        } else if(name.length() > 30) {
            errors=true;
            nameError = "Name must be 30 characters or less.";
        }
        if(description.isBlank()) {
            errors=true;
            descError = "Description must not be blank.";
        } else if(description.length() > 255) {
            errors=true;
            descError = "Description must be 255 characters or less.";
        }
        if(orgIds == null) {
            errors = true;
            orgError = "Hero/Villain must be associated with at least one organization.";
        }

        if(!errors) {
            Hero hero = new Hero();
            hero.setName(name);
            hero.setDescription(description);
            hero.setSuperPower(power);
            hero = heroDao.addHero(hero);
            for(String o : orgIds) {
                organizationDao.addHeroToOrg(organizationDao.getOrganizationById(Integer.parseInt(o)), hero);
            }
        }

        return "redirect:/heroes";
    }

    /*@PostMapping("editHero")
    public String performEditHero(@Valid Hero hero, BindingResult result) {
        if(result.hasErrors()) {
            errors = result.getFieldErrors();
            return "editHeroes";
        } else {
            heroDao.updateHero(hero);
            return "redirect:/heroes";
        }
    }*/

    @PostMapping("editHero")
    public String performEditHero(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("heroId"));
        String name = request.getParameter("heroName");
        String description = request.getParameter("heroDescription");
        SuperPower power = superPowerDao.getSuperPowerById(Integer.parseInt(request.getParameter("superPower")));
        String[] orgIds = request.getParameterValues("orgId");

        boolean errors = false;

        if(name.isBlank()) {
            errors=true;
            nameError = "Name must not be blank.";
        } else if(name.length() > 30) {
            errors=true;
            nameError = "Name must be 30 characters or less.";
        }
        if(description.isBlank()) {
            errors=true;
            descError = "Description must not be blank.";
        } else if(description.length() > 255) {
            errors=true;
            descError = "Description must be 255 characters or less.";
        }
        if(orgIds == null) {
            errors = true;
            orgError = "Hero/Villain must be associated with at least one organization.";
        }

        if(!errors) {
            Hero hero = new Hero();
            hero.setId(id);
            hero.setName(name);
            hero.setDescription(description);
            hero.setSuperPower(power);
            heroDao.updateHero(hero);
            for(String o : orgIds) {
                organizationDao.addHeroToOrg(organizationDao.getOrganizationById(Integer.parseInt(o)), hero);
            }
            return "redirect:/heroes";
        } else {
            return "redirect:/editHeroes";
        }
    }

    private List<SuperPower> createDefaultPower() {
        SuperPower normal = new SuperPower();
        normal.setSuperPowerName("Normal Guy");
        superPowerDao.addSuperPower(normal);
        return superPowerDao.getAllSuperPowers();
    }

    private List<Organization> createDefaultOrg() {
        Organization organization = new Organization();
        organization.setName("No Affiliation");
        organization.setDescription("N/A");
        organization.setAddress("N/A");
        organization.setEmail("N/A");
        organizationDao.addOrganization(organization);
        return organizationDao.getAllOrganizations();
    }
}
