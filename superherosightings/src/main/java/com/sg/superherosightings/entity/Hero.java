package com.sg.superherosightings.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Hero {

    private int id;

    @NotBlank(message = "Name must not be blank.")
    @Size(max = 30, message = "Name must be 30 characters or less.")
    private String name;

    @NotBlank(message = "Description must not be blank.")
    @Size(max = 255, message = "Description must be 255 characters or less.")
    private String description;

    @NotNull(message = "Must have a super power.")
    private SuperPower superPower;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SuperPower getSuperPower() {
        return superPower;
    }

    public void setSuperPower(SuperPower superPower) {
        this.superPower = superPower;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hero hero = (Hero) o;

        if (getId() != hero.getId()) return false;
        if (!getName().equals(hero.getName())) return false;
        if (!getDescription().equals(hero.getDescription())) return false;
        return getSuperPower().equals(hero.getSuperPower());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + getSuperPower().hashCode();
        return result;
    }
}
