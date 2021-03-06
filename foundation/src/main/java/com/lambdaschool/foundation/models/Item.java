package com.lambdaschool.foundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "items")
public class Item extends Auditable {

  /**
   * The primary key (long) of the items table
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long itemid;

  @Column(nullable = false)
  private String itemname;


  /**
   * The potluckid of the potluck attached to this item is what is stored in the database.
   * This is the entire potluck object!
   * <p>
   * Forms a Many to One relationship between items and potlucks.
   * A potluck can have many items.
   */
  @ManyToOne
  @JoinColumn(name="potluckid", nullable = false)
  @JsonIgnoreProperties(value="items", allowSetters = true)
  private Potluck potluck;

  /**
   * The userid of person who signed up for this item is what is stored in the database.
   * This is the entire user object!
   * <p>
   * Forms a Many to One relationship between items and users.
   * A user can have many items.
   */
  @ManyToOne
  @JoinColumn(name = "userid",
      nullable = true)
  @JsonIgnoreProperties(value= {"potlucksattending"},
      allowSetters = true)
  private User user;

  /**
   * Default constructor used primarily by the JPA.
   */
  public Item() {
  }

  /**
   * Given the params, create a new potluck object
   * <p>
   * userid is autogenerated
   *
   * @param itemname The name (String) of the potluck
   */
  public Item(String itemname) {
    this.itemname = itemname;
  }

  public long getItemid() {
    return itemid;
  }

  public void setItemid(long itemid) {
    this.itemid = itemid;
  }

  public String getItemname() {
    return itemname;
  }

  public void setItemname(String itemname) {
    this.itemname = itemname;
  }

  public Potluck getPotluck() {
    return potluck;
  }

  public void setPotluck(Potluck potluck) {
    this.potluck = potluck;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
