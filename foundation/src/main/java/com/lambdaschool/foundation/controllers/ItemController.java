package com.lambdaschool.foundation.controllers;

import com.lambdaschool.foundation.models.Item;
import com.lambdaschool.foundation.models.User;
import com.lambdaschool.foundation.services.ItemService;
import com.lambdaschool.foundation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
public class ItemController {

  @Autowired
  private ItemService itemService;

  @Autowired
  private UserService userService;

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


}
