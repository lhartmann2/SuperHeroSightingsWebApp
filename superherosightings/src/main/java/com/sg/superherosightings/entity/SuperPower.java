package com.sg.superherosightings.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SuperPower {

    private int id;

    @NotBlank(message = "Superpower name must not be blank.")
    @Size(max = 30, message = "Superpower name must be 30 characters or less.")
    private String superPowerName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSuperPowerName() {
        return superPowerName;
    }

    public void setSuperPowerName(String superPowerName) {
        this.superPowerName = superPowerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SuperPower that = (SuperPower) o;

        if (getId() != that.getId()) return false;
        return getSuperPowerName().equals(that.getSuperPowerName());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getSuperPowerName().hashCode();
        return result;
    }
}
