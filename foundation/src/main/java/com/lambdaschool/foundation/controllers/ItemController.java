package com.lambdaschool.foundation.controllers;

import com.lambdaschool.foundation.exceptions.ResourceFoundException;
import com.lambdaschool.foundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.foundation.models.Item;
import com.lambdaschool.foundation.models.User;
import com.lambdaschool.foundation.services.ItemService;
import com.lambdaschool.foundation.services.PotluckService;
import com.lambdaschool.foundation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ItemController {

  @Autowired
  private ItemService itemService;

  @Autowired
  private UserService userService;

  @Autowired
  private PotluckService potluckService;

  @PreAuthorize("isAuthenticated()")
  @PostMapping(value = "/api/users/{userid}/potlucks/{potluckid}/items", consumes = "application/json", produces = "application/json")
  public ResponseEntity<?> createNewItem(
      @Valid
    @RequestBody Item newitem,
      @PathVariable Long potluckid) throws URISyntaxException {
    newitem.setItemid(0);

    User user = userService.findByName(
        SecurityContextHolder.getContext().getAuthentication().getName()
    );

    newitem = itemService.save(newitem, user.getUserid(), potluckid);

    return new ResponseEntity<>(newitem, HttpStatus.CREATED);
  }

  //
  @PreAuthorize("isAuthenticated()")
  @PutMapping(value = "/api/users/{userid}/potlucks/{potluckid}/items/{itemid}", consumes = "application/json", produces = "application/json")
  public ResponseEntity<?> updateItem(
    @Valid
    @RequestBody Item updatedItem,
    @PathVariable Long potluckid,
    @PathVariable Long itemid
  ) {
    User user = userService.findByName(
        SecurityContextHolder.getContext().getAuthentication().getName()
    );
    if (user.getUserid() == potluckService.findPotluckById(potluckid).getUser().getUserid()) {
      updatedItem.setItemid(itemid);
      itemService.save(updatedItem, user.getUserid(), potluckid);
    } else {
      return new ResponseEntity<>("Items can only be updated by potluck creator.", HttpStatus.FORBIDDEN);
    }
    Item returnItem = itemService.findItemById(itemid);

    return new ResponseEntity<>(returnItem, HttpStatus.OK);
  }

  //GET ALL ITEMS ACROSS DATABASE
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/api/items", produces = "application/json")
  public ResponseEntity<?> findAllItems() {
    List<Item> allItems = new ArrayList<>(itemService.findAll());

    return new ResponseEntity<>(allItems, HttpStatus.OK);
  }

  //GET ALL ITEMS FOR POTLUCK ID
  @GetMapping(value = "/api/users/{id}/potlucks/{potluckid}/items", produces = "application/json")
  public ResponseEntity<?> findItemsByPotluckId(
      @PathVariable Long potluckid) {
    List<Item> itemsForPotluck = itemService.findItemsByPotluckId(potluckid);

    return new ResponseEntity<>(itemsForPotluck, HttpStatus.OK);
  }

  //DELETE POTLUCK ITEM BY ID
  @DeleteMapping(value = "/api/users/{userid}/potlucks/{potluckid}/items/{itemid}")
  public ResponseEntity<?> deleteItem(
      @PathVariable Long itemid
  ) {
        User user = userService.findByName(
            SecurityContextHolder.getContext().getAuthentication().getName()
        );
        if (user.getUserid() == itemService.findItemById(itemid).getUser().getUserid()) {
          itemService.delete(itemid);
        } else {
          return new ResponseEntity<>("Item can only be deleted by potluck creator.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
  }

  //USER CAN ADD THEMSELVES TO AN ITEM
  @PatchMapping(value = "/api/potlucks/{potluckid}/items/{itemid}")
  public ResponseEntity<?> updateItemUser(
    @PathVariable long potluckid,
    @PathVariable long itemid
  ) {

    User user = userService.findByName(
        SecurityContextHolder.getContext().getAuthentication().getName()
    );
    System.out.println("CONTROLLER" + user.getUserid());
    System.out.println("CONTROLLER" + user.getUsername());
    System.out.println("CONTROLLER" + potluckid);
    System.out.println("CONTROLLER" + itemid);
    itemService.addUserToItem(user.getUserid(), potluckid, itemid);

    return new ResponseEntity<>("User " + user.getUserid() + " signed up to bring item " + itemid + "!", HttpStatus.OK);
  }

}
