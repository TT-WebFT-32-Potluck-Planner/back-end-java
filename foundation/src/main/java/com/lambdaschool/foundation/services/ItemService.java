package com.lambdaschool.foundation.services;

import com.lambdaschool.foundation.models.Item;

import java.util.List;


public interface ItemService {
  List<Item> findAll();

  Item findItemById(long id);

  Item save(Item item, long userid, long potluckid);
}