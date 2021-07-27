package com.sg.superherosightings.controllers;

import com.sg.superherosightings.dao.HeroDao;
import com.sg.superherosightings.dao.OrganizationDao;
import com.sg.superherosightings.entity.Hero;
import com.sg.superherosightings.entity.Organization;
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
public class OrganizationController {

    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    HeroDao heroDao;

    private List<FieldError> errors;
    private Set<ConstraintViolation<Organization>> violations;

    public OrganizationController() {
        errors = new ArrayList<>();
        violations = new HashSet<>();
    }

    @GetMapping("organizations")
    public String displayOrgs(Model model) {
        List<Organization> orgs = organizationDao.getAllOrganizations();
        List<Hero> heroes = heroDao.getAllHeroes();
        if(orgs.isEmpty()) { //Add default
            orgs = createDefaultOrg();
        }
        model.addAttribute("orgs", orgs);
        model.addAttribute("heroes", heroes);
        model.addAttribute("errors", violations);

        return "organizations";
    }

    @GetMapping("deleteOrganization")
    public String deleteOrg(int id, Model model) {
        Organization org = organizationDao.getOrganizationById(id);
        if(!(org.getName().equalsIgnoreCase("No Affiliation"))){
            List<Hero> heroes = heroDao.listHeroesInOrg(org);
            model.addAttribute("org", org);
            model.addAttribute("heroes", heroes);
            return "deleteOrganization";
        }
        return "redirect:/organizations";
    }

    @GetMapping("performDeleteOrg")
    public String performDeleteOrg(int id) {
        Organization testCase = organizationDao.getOrganizationById(id);
        if(!(testCase.getName().equalsIgnoreCase("No Affiliation"))) {
            organizationDao.deleteOrganizationById(id);
        }
        return "redirect:/organizations";
    }

    @GetMapping("editOrganization")
    public String editOrg(Integer id, Model model) {
        Organization org = organizationDao.getOrganizationById(id);
        if(!(org.getName().equalsIgnoreCase("No Affiliation"))) {
            model.addAttribute("organization", org);
            return "editOrganization";
        } else {
            return "redirect:/organizations";
        }
    }

    @GetMapping("organizationsByHero")
    public String displayOrgsByHero(Integer id, Model model) {
        Hero hero = heroDao.getHeroById(id);
        List<Organization> orgs = organizationDao.getAllOrgsOfHero(hero);
        List<Hero> heroes = heroDao.getAllHeroes();
        model.addAttribute("orgs", orgs);
        model.addAttribute("hero", hero);
        model.addAttribute("heroes", heroes);
        return "organizationsByHero";
    }

    @GetMapping("organizationDetail")
    public String orgDetail(Integer id, Model model) {
        Organization org = organizationDao.getOrganizationById(id);
        List<Hero> heroes = heroDao.listHeroesInOrg(org);
        model.addAttribute("organization", org);
        model.addAttribute("heroes", heroes);
        return "organizationDetail";
    }

    @PostMapping("addOrg")
    public String addOrg(HttpServletRequest request) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String address = request.getParameter("address");
        String email = request.getParameter("email");

        Organization org = new Organization();
        org.setName(name);
        org.setDescription(description);
        org.setAddress(address);
        org.setEmail(email);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(org);

        if(violations.isEmpty()) {
            organizationDao.addOrganization(org);
        }
        return "redirect:/organizations";
    }

    @PostMapping("editOrganization")
    public String performEditOrg(@Valid Organization org, BindingResult result) {
        if(result.hasErrors()) {
            errors = result.getFieldErrors();
            return "editOrganization";
        } else {
            organizationDao.updateOrganization(org);
            return "redirect:/organizations";
        }
    }

    private List<Organization> createDefaultOrg() {
        Organization defaultOrg = new Organization();
        defaultOrg.setName("No Affiliation");
        defaultOrg.setDescription("N/A");
        defaultOrg.setAddress("N/A");
        defaultOrg.setEmail("N/A");
        organizationDao.addOrganization(defaultOrg);
        return organizationDao.getAllOrganizations();
    }
}
