package com.lambdaschool.foundation.services;

import com.lambdaschool.foundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.foundation.models.Attendee;
import com.lambdaschool.foundation.models.Item;
import com.lambdaschool.foundation.models.Potluck;
import com.lambdaschool.foundation.models.User;
import com.lambdaschool.foundation.repository.ItemRepository;
import com.lambdaschool.foundation.repository.PotluckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "itemService")
public class ItemServiceImpl implements ItemService{

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private PotluckService potluckService;

  @Autowired
  private UserService userService;


  @Override
  public List<Item> findAll() {
    List<Item> allItems = new ArrayList<>();
    itemRepository.findAll().forEach(allItems::add);
    return allItems;
  }

  @Override
  public Item findItemById(long id) {
    return itemRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Item id " + id + " not found!"));
  }

  @Transactional
  @Override
  public Item save(Item item, long userid, long potluckid) {
    Item newItem = new Item();

    if (item.getItemid() != 0)
    {
      itemRepository.findById(item.getItemid())
          .orElseThrow(() -> new ResourceNotFoundException("Item id " + item.getItemid() + " not found!"));
      newItem.setItemid(item.getItemid());
    }

    newItem.setItemname(item.getItemname());

    newItem.setPotluck(potluckService.findPotluckById(potluckid));

    newItem.setUser(item.getUser());

    return itemRepository.save(newItem);
  }


  @Override
  public List<Item> findItemsByPotluckId(Long potluckid) {
    List<Item> potluckItems = new ArrayList<>();
        itemRepository.findItemsByPotluck(potluckService.findPotluckById(potluckid))
            .iterator()
            .forEachRemaining(potluckItems::add);
    return potluckItems;
  }

  @Transactional
  @Override
  public void delete(long id) {
    itemRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Item id " + id + " not found!"));
    itemRepository.deleteById(id);
  }

  @Transactional
  @Override
  public Item addUserToItem(long userid, long potluckid, long itemid) {

    Item newItem = findItemById(itemid);
    newItem.setUser(userService.findUserById(userid));

    return itemRepository.save(newItem);
  }
}
