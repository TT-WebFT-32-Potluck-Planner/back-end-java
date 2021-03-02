package com.lambdaschool.foundation.services;

import com.lambdaschool.foundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.foundation.models.*;
import com.lambdaschool.foundation.repository.PotluckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "potluckService")
public class PotluckServiceImpl implements PotluckService{

  @Autowired
  private PotluckRepository potluckRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private ItemService itemService;

  @Override
  public List<Potluck> findAll() {
    List<Potluck> allPotlucks = new ArrayList<>();
    potluckRepository.findAll().iterator().forEachRemaining(allPotlucks::add);
    return allPotlucks;
  }


  @Override
  public List<Potluck> findPotlucksByUser(long id) {

    //potlucks hosted by user
    List<Potluck> potlucks = potluckRepository.findPotluckByUser(userService.findUserById(id));

    return potlucks;
  }

  @Override
  public Potluck findPotluckByName(String name) {
    Potluck potluck = potluckRepository.findPotluckByPotluckname(name);

    return potluck;
  }

  @Transactional
  @Override
  public Potluck save(Potluck potluck, long userid) {
    Potluck newPotluck = new Potluck();

    if (potluck.getPotluckid() != 0)
    {
      potluckRepository.findById(potluck.getPotluckid())
          .orElseThrow(() -> new ResourceNotFoundException("Potluck id " + potluck.getPotluckid() + " not found!"));
      newPotluck.setPotluckid(potluck.getPotluckid());
    }

    //check if name already exists
//    potluck.getPotluckname()

    newPotluck.setPotluckname(potluck.getPotluckname());
    newPotluck.setDate(potluck.getDate());
    newPotluck.setTime(potluck.getTime());
    newPotluck.setLocation(potluck.getLocation());

    newPotluck.setUser(userService.findUserById(userid));

    newPotluck.getAttendees()
        .clear();



    for (Attendee attendee : potluck.getAttendees())
    {
      User addUser = userService.findUserById(attendee.getUser().getUserid());
      newPotluck.getAttendees()
          .add(new Attendee(addUser, newPotluck));
    }

    newPotluck.getPotLuckitems()
        .clear();

    for (PotLuckItem potLuckItem : potluck.getPotLuckitems())
    {
      Item addItem = itemService.findItemById(potLuckItem.getItem().getItemid());
      newPotluck.getPotLuckitems()
          .add(new PotLuckItem(newPotluck, addItem));
    }

    return potluckRepository.save(newPotluck);
  }

  @Override
  public Potluck findPotluckById(long id) {
    Potluck potluck = potluckRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Potluck id " + " not found!"));
    return potluck;
  }

  @Override
  public void deletePotluckById(long id) {
    potluckRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Potluck id " + id + " not found!"));
    potluckRepository.deleteById(id);
  }
}
