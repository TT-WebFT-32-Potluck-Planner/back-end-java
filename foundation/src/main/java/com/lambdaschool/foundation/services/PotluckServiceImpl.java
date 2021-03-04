package com.lambdaschool.foundation.services;

import com.lambdaschool.foundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.foundation.models.*;
import com.lambdaschool.foundation.repository.PotluckRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private HelperFunctions helperFunctions;

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
      public Potluck update(
          Potluck potluck,
          long potluckid)
      {
          Potluck currentPotluck = findPotluckById(potluckid);
              currentPotluck.setPotluckname(potluck.getPotluckname());
              currentPotluck.setDate(potluck.getDate());
              currentPotluck.setTime(potluck.getTime());
              currentPotluck.setLocation(potluck.getLocation());

          for (Attendee attendee : potluck.getAttendees())
          {
            User addUser = userService.findUserById(attendee.getAttendee().getUserid());
            currentPotluck.getAttendees()
                .add(new Attendee(addUser, currentPotluck));
          }

          currentPotluck.getItems()
              .clear();

          for (Item item : potluck.getItems())
          {
            Item addItem = itemService.findItemById(item.getItemid());
            currentPotluck.getItems()
                .add(addItem);
          }


              return potluckRepository.save(currentPotluck);
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

    if (potluckRepository.findPotluckByPotluckname(potluck.getPotluckname()) != null)
      throw new ResourceNotFoundException("Potluck name " + potluck.getPotluckname() + " already exists.");

    newPotluck.setPotluckname(potluck.getPotluckname());
    newPotluck.setDate(potluck.getDate());
    newPotluck.setTime(potluck.getTime());
    newPotluck.setLocation(potluck.getLocation());

    newPotluck.setUser(userService.findUserById(userid));

    newPotluck.getAttendees()
        .clear();



    for (Attendee attendee : potluck.getAttendees())
    {
      User addUser = userService.findUserById(attendee.getAttendee().getUserid());
      newPotluck.getAttendees()
          .add(new Attendee(addUser, newPotluck));
    }

    newPotluck.getItems()
        .clear();

    for (Item item : potluck.getItems())
    {
      Item addItem = itemService.findItemById(item.getItemid());
      newPotluck.getItems()
          .add(addItem);
    }

    return potluckRepository.save(newPotluck);
  }

  @Override
  public Potluck findPotluckById(long id) {
    Potluck potluck = potluckRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Potluck id " + " not found!"));
    return potluck;
  }

  @Transactional
  @Override
  public void deletePotluckById(long id) {
    potluckRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Potluck id " + id + " not found!"));
    potluckRepository.deleteById(id);
  }

  @Override
  public void RSVPForPotluck(User user, long potluckid) {
    Potluck potluck = potluckRepository.findById(potluckid)
        .orElseThrow(() -> new ResourceNotFoundException("Potluck id " + potluckid + " not found!"));

        potluck.getAttendees().add(new Attendee(user, potluck));
//        user.getAttendees().add(newAttendee);

//        update(potluck, potluckid);
  }
}
