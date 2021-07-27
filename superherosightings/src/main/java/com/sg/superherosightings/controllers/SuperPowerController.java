package com.sg.superherosightings.controllers;

import com.sg.superherosightings.dao.SuperPowerDao;
import com.sg.superherosightings.entity.SuperPower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class SuperPowerController {

    @Autowired
    SuperPowerDao superPowerDao;

    private Set<ConstraintViolation<SuperPower>> violations;

    public SuperPowerController() {
        violations = new HashSet<>();
    }

    @GetMapping("superPowers")
    public String displaySuperPowers(Model model) {
        List<SuperPower> powerList = superPowerDao.getAllSuperPowers();
        if(powerList.isEmpty()) {
            SuperPower normal = new SuperPower();
            normal.setSuperPowerName("Normal Guy");
            superPowerDao.addSuperPower(normal);
            powerList = superPowerDao.getAllSuperPowers();
        }
        model.addAttribute("superPowers", powerList);
        model.addAttribute("errors", violations);
        return "superPowers";
    }

    @GetMapping("deleteSuperPower")
    public String deleteSuperPower(int id, Model model) {
        SuperPower superPower = superPowerDao.getSuperPowerById(id); //Don't delete default
        if((superPower.getSuperPowerName().equalsIgnoreCase("Normal Guy"))) {
            return "redirect:/superPowers";
        }
        model.addAttribute("power", superPower);
        return "deleteSuperPower";
    }

    @GetMapping("editSuperPower")
    public String editSuperPower(Integer id, Model model) {
        SuperPower superPower = superPowerDao.getSuperPowerById(id);
        if(!(superPower.getSuperPowerName().equalsIgnoreCase("Normal Guy"))) {
            model.addAttribute("superPower", superPower);
            return "editSuperPower";
        } else {
            return "redirect:/superPowers";
        }
    }

    @GetMapping("superPowerDetail")
    public String displayPowerDetail(Integer id, Model model) {
        SuperPower superPower = superPowerDao.getSuperPowerById(id);
        model.addAttribute("superpower", superPower);
        return "superPowerDetail";
    }

    @PostMapping("addSuperPower")
    public String addSuperPower(HttpServletRequest request) {
        SuperPower superPower = new SuperPower();
        superPower.setSuperPowerName(request.getParameter("superPowerName"));

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validator.validate(superPower);

        if(violations.isEmpty()) {
            superPowerDao.addSuperPower(superPower);
        }
        return "redirect:/superPowers";
    }

    @PostMapping("editSuperPower")
    public String performEditSuperPower(@Valid SuperPower superPower, BindingResult result) {
        if(result.hasErrors()) {
            return "editSuperPower";
        }
        superPowerDao.updateSuperPower(superPower);
        return "redirect:/superPowers";
    }

    @GetMapping("performDeletePower")
    public String performDeletePower(int id) {
        SuperPower testCase = superPowerDao.getSuperPowerById(id); //Don't delete default
        if(!(testCase.getSuperPowerName().equalsIgnoreCase("Normal Guy"))) {
            superPowerDao.deleteSuperPowerById(id);
        }
        return "redirect:/superPowers";
    }
}
