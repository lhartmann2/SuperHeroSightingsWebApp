package com.sg.superherosightings.dao;

import com.sg.superherosightings.entity.SuperPower;

import java.util.List;

public interface SuperPowerDao {

    SuperPower addSuperPower(SuperPower superPower);
    SuperPower getSuperPowerById(int id);
    void updateSuperPower(SuperPower superPower);
    void deleteSuperPowerById(int id);
    List<SuperPower> getAllSuperPowers();

}
