package com.lambdaschool.foundation.controllers;

import com.lambdaschool.foundation.models.Potluck;
import com.lambdaschool.foundation.models.User;
import com.lambdaschool.foundation.services.PotluckService;
import com.lambdaschool.foundation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class PotluckController {

  @Autowired
  private PotluckService potluckService;

  @Autowired
  private UserService userService;

//    @PreAuthorize("hasAnyRole('ADMIN')")
//    @GetMapping(value = "/users",
//        produces = "application/json")
//    public ResponseEntity<?> listAllUsers()
//    {
//        List<User> myUsers = userService.findAll();
//        return new ResponseEntity<>(myUsers,
//            HttpStatus.OK);
//    }

  /**
   * Returns a list of all potlucks
   * <br>Example: <a href="http://localhost:2019/api/potlucks">http://localhost:2019/api/potlucks</a>
   *
   * @return JSON list of all potlucks with a status of OK
   * @see PotluckService#findAll() PotluckService.findAll()
   */
  @GetMapping(value = "/api/potlucks", produces = "application/json")
  public ResponseEntity<?> listAllPotlucks() {
    List<Potluck> allPotlucks = potluckService.findAll();
    return new ResponseEntity<>(allPotlucks, HttpStatus.OK);
  }

  /**
   * Returns a list of potlucks by user id, auth required
   * <br>Example: <a href="http://localhost:2019/api/users/{userid}/potlucks">http://localhost:2019/api/users/{userid}/potlucks</a>
   *
   * @return eturns all potlucks user is apart of, created or otherwise! with a status of OK
   * @see PotluckService#findPotlucksByUser(long id) PotluckService.findPotlucksByUser()
   */
  //get all potlucks user is hosting
  @GetMapping(value = "/api/users/{id}/potlucks", produces = "application/json")
  public ResponseEntity<?> listPotlucksByUser(@PathVariable Long id) {
    List<Potluck> potlucks = potluckService.findPotlucksByUser(id);
    return new ResponseEntity<>(potlucks, HttpStatus.OK);
  }

  //find potluck by id
  @GetMapping(value = "/api/potlucks/potluckid/{potluckid}", produces = "application/json")
  public ResponseEntity<?> findPotluckById(@PathVariable Long potluckid) {
    Potluck potluck = potluckService.findPotluckById(potluckid);
    return new ResponseEntity<>(potluck, HttpStatus.OK);
  }

  //create new potluck
  @PreAuthorize("isAuthenticated()")
  @PostMapping(value = "/api/users/{userid}/potlucks", produces = "application/json", consumes = "application/json")
  public ResponseEntity<?> addNewPotluck(
    @Valid
    @RequestBody Potluck newPotluck
    ) throws URISyntaxException {
    User user = userService.findByName(
        SecurityContextHolder.getContext().getAuthentication().getName()
    );
    newPotluck.setPotluckid(0);
    newPotluck = potluckService.save(newPotluck, user.getUserid());

    // set the location header for the newly created resource
    HttpHeaders responseHeaders = new HttpHeaders();
    URI newPotluckURI = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/api/potlucks/potluckid/{potluckid}")
        .buildAndExpand(newPotluck.getPotluckid())
        .toUri();
    responseHeaders.setLocation(newPotluckURI);

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  //PUT TO UPDATE POTLUCK AS ORGANIZER: /api/users/:id/potlucks/:potId
  @PreAuthorize("isAuthenticated()")
  @PutMapping(value = "/api/users/{userid}/potlucks/{potluckid}", produces = "application/json", consumes = "application/json")
  public ResponseEntity<?> updatePotluck(
      @Valid
      @RequestBody Potluck updatePotluck,
      @PathVariable long potluckid
  ) throws URISyntaxException {
    User user = userService.findByName(
        SecurityContextHolder.getContext().getAuthentication().getName()
    );
    updatePotluck.setPotluckid(potluckid);

    if (user.getUserid() == potluckService.findPotluckById(potluckid).getUser().getUserid()) {
      potluckService.save(updatePotluck, user.getUserid());
    } else {
      return new ResponseEntity<>("Potluck can only be updated by potluck creator.", HttpStatus.FORBIDDEN);
    }

    Potluck updatedPotluck = potluckService.findPotluckById(potluckid);

    return new ResponseEntity<>(updatedPotluck, HttpStatus.OK);
  }

  //find potluck by name
  @GetMapping(value = "/api/potlucks/name/{potluckname}", produces = "application/json")
  public ResponseEntity<?> findPotluckByName(@PathVariable String potluckname) {
    Potluck potluck = potluckService.findPotluckByName(potluckname);
    return new ResponseEntity<>(potluck, HttpStatus.OK);
  }

  //delete potluck
  @DeleteMapping(value = "/api/users/{id}/potlucks/{potluckid}")
  public ResponseEntity<?> deletePotluck(@PathVariable Long potluckid) {
    User user = userService.findByName(
        SecurityContextHolder.getContext().getAuthentication().getName()
    );
    if (user.getUserid() == potluckService.findPotluckById(potluckid).getUser().getUserid()) {
      potluckService.deletePotluckById(potluckid);
    } else {
      return new ResponseEntity<>("Potluck can only be deleted by potluck creator.", HttpStatus.FORBIDDEN);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
