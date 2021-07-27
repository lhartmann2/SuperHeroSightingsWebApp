package com.sg.superherosightings.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class Organization {

    private int id;

    @NotBlank(message = "Organization name must not be blank.")
    @Size(max = 30, message = "Organization name must be 30 characters or less.")
    private String name;

    @NotBlank(message = "Organization description must not be blank.")
    @Size(max = 255, message = "Organization description must be 255 characters or less.")
    private String description;

    @NotBlank(message = "Organization address must not be blank.")
    @Size(max = 128, message = "Organization address must be 128 characters or less.")
    private String address;

    @NotBlank(message = "Organization email must not be blank.")
    @Size(max = 64, message = "Organization email must be 64 characters or less.")
    @Email(message = "Must be a valid email.")
    private String email;

    List<Hero> members;

    public void clear() {
        members.clear();
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Hero> getMembers() {
        return members;
    }

    public void setMembers(List<Hero> members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (getId() != that.getId()) return false;
        if (!getName().equals(that.getName())) return false;
        if (!getDescription().equals(that.getDescription())) return false;
        if (!getAddress().equals(that.getAddress())) return false;
        return getEmail().equals(that.getEmail());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + getAddress().hashCode();
        result = 31 * result + getEmail().hashCode();
        return result;
    }
}
