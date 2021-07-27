package com.sg.superherosightings.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Location {

    private int id;

    @NotBlank(message = "Location name must not be blank.")
    @Size(max = 30, message = "Location name must be 30 characters or less.")
    private String name;

    @NotBlank(message = "Location description must not be blank.")
    @Size(max = 255, message = "Location description must be 255 characters or less.")
    private String description;

    @Size(max = 128, message = "Location address must be 128 characters or less.")
    private String address;

    @NotBlank(message = "Location latitude must not be blank.")
    @Size(max = 24, message = "Location latitude must be 24 characters or less.")
    private String latitude;

    @NotBlank(message = "Location longitude must not be blank.")
    @Size(max = 24, message = "Location longitude must be 24 characters or less.")
    private String longitude;

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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (getId() != location.getId()) return false;
        if (getName() != null ? !getName().equals(location.getName()) : location.getName() != null) return false;
        if (getDescription() != null ? !getDescription().equals(location.getDescription()) : location.getDescription() != null)
            return false;
        if (getAddress() != null ? !getAddress().equals(location.getAddress()) : location.getAddress() != null)
            return false;
        if (getLatitude() != null ? !getLatitude().equals(location.getLatitude()) : location.getLatitude() != null)
            return false;
        return getLongitude() != null ? getLongitude().equals(location.getLongitude()) : location.getLongitude() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = 31 * result + (getLatitude() != null ? getLatitude().hashCode() : 0);
        result = 31 * result + (getLongitude() != null ? getLongitude().hashCode() : 0);
        return result;
    }
}
