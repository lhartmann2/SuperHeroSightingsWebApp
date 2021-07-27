package com.sg.superherosightings.dao;

import com.sg.superherosightings.entity.Hero;
import com.sg.superherosightings.entity.Organization;

import java.util.List;

public interface OrganizationDao {

    Organization addOrganization(Organization organization);
    Organization getOrganizationById(int id);
    void updateOrganization(Organization organization);
    void deleteOrganizationById(int id);
    List<Organization> getAllOrganizations();

    Organization addHeroToOrg(Organization organization, Hero hero);

    void clearMembers(Organization organization);

    List<Organization> getAllOrgsOfHero(Hero hero);
}
