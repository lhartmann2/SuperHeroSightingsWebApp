package com.sg.superherosightings.dao;

import com.sg.superherosightings.entity.Hero;
import com.sg.superherosightings.entity.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrganizationDaoDB implements OrganizationDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Organization addOrganization(Organization organization) {
        final String ADD_ORG = "INSERT INTO org(orgName, orgDescription, orgAddress, orgEmail) " +
                "VALUES(?,?,?,?)";
        jdbc.update(ADD_ORG,
                organization.getName(),
                organization.getDescription(),
                organization.getAddress(),
                organization.getEmail());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        organization.setId(newId);
        organization.setMembers(associateHeroes(organization));
        return organization;
    }

    @Override
    public Organization getOrganizationById(int id) {
        try {
            final String GET_ORG_BY_ID = "SELECT * FROM org WHERE id = ?";
            Organization organization = jdbc.queryForObject(GET_ORG_BY_ID, new OrganizationMapper(), id);
            organization.setMembers(associateHeroes(organization));
            return organization;
        } catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public void updateOrganization(Organization organization) {
        final String UPDATE_ORG = "UPDATE org SET orgName = ?, orgDescription = ?, " +
                "orgAddress = ?, orgEmail = ? WHERE id = ?";
        jdbc.update(UPDATE_ORG,
                organization.getName(),
                organization.getDescription(),
                organization.getAddress(),
                organization.getEmail(),
                organization.getId());
    }

    @Override
    @Transactional
    public void deleteOrganizationById(int id) {
        //Delete from org_hero
        final String DELETE_ORG_HERO = "DELETE FROM org_hero WHERE orgId = ?";
        jdbc.update(DELETE_ORG_HERO, id);

        //Delete org
        final String DELETE_ORG = "DELETE FROM org WHERE id = ?";
        jdbc.update(DELETE_ORG, id);
    }

    @Override
    public List<Organization> getAllOrganizations() {
        List<Organization> orgs = jdbc.query("SELECT * FROM org", new OrganizationMapper());
        for(Organization o : orgs) {
            o.setMembers(associateHeroes(o));
        }
        return orgs;
    }

    @Override
    public List<Organization> getAllOrgsOfHero(Hero hero) {
        final String GET_ORGS_OF_HERO = "SELECT o.* FROM org o JOIN " +
                "org_hero h ON o.id = h.orgId WHERE h.heroId = ?";
        return jdbc.query(GET_ORGS_OF_HERO, new OrganizationMapper(), hero.getId());
    }

    @Override
    public Organization addHeroToOrg(Organization organization, Hero hero) {
        final String ADD_HERO_TO_ORG = "INSERT IGNORE INTO org_hero(orgId, heroId) " +
                "VALUES(?,?)";
        jdbc.update(ADD_HERO_TO_ORG,
                organization.getId(), hero.getId());
        clearMembers(organization);
        organization.setMembers(associateHeroes(organization));
        return organization;
    }

    @Override
    public void clearMembers(Organization organization) {
        organization.clear();
    }

    private List<Hero> associateHeroes(Organization organization) {
        final String GET_ORG_HERO = "SELECT h.* FROM hero h JOIN " +
                "org_hero o ON h.id = o.heroId WHERE o.orgId = ?";
        return jdbc.query(GET_ORG_HERO, new HeroDaoDB.HeroMapper(), organization.getId());
    }

    public static final class OrganizationMapper implements RowMapper<Organization> {
        @Override
        public Organization mapRow(ResultSet rs, int index) throws SQLException {
            Organization organization = new Organization();
            organization.setId(rs.getInt("id"));
            organization.setName(rs.getString("orgName"));
            organization.setDescription(rs.getString("orgDescription"));
            organization.setAddress(rs.getString("orgAddress"));
            organization.setEmail(rs.getString("orgEmail"));
            return organization;
        }
    }
}
