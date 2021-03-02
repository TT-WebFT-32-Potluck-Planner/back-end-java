package com.lambdaschool.foundation.repository;

import com.lambdaschool.foundation.models.Item;
import com.lambdaschool.foundation.models.Potluck;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {

  //User findByUsername(String username);
  List<Item> findItemsByPotluck(Potluck potluck);
}
