package com.lambdaschool.foundation.services;

import com.lambdaschool.foundation.models.Potluck;

import java.util.List;

public interface PotluckService {
  List<Potluck> findAll();

  List<Potluck> findPotlucksByUser(long id);

  Potluck save(Potluck potluck, long userid);
  Potluck update(Potluck potluck, long potluckid);

  Potluck findPotluckById(long id);

  Potluck findPotluckByName(String name);

  void deletePotluckById(long id);
}
