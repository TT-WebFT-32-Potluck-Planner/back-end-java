package com.lambdaschool.foundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "potluckitems")
public class PotLuckItem {

  /**
   * The primary key (long) of the potluckitems table
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long potluckitemid;

  /**
   * The potluckid of the potluck attached to this potluckitem is what is stored in the database.
   * This is the entire potluck object!
   * <p>
   * Forms a Many to One relationship between potluckitems and potlucks.
   * A potluck can have many potluckitems.
   */
  @ManyToOne
  @JoinColumn(name="potluckid", nullable = false)
  @JsonIgnoreProperties(value="potluckitems")
  private Potluck potluck;

  /**
   * The itemid of the item attached to this potluckitem is what is stored in the database.
   * This is the entire item object!
   * <p>
   * Forms a Many to One relationship between potluckitems and items.
   * An item can have many potluckitems.
   */
  @ManyToOne
  @JoinColumn(name="itemid", nullable = false)
  @JsonIgnoreProperties(value = "potluckitems")
  private Item item;

  /**
   * The userid of person who signed up for this item is what is stored in the database.
   * This is the entire user object!
   * <p>
   * Forms a Many to One relationship between potluckitems and users.
   * A user can have many potluckitems.
   */
  @ManyToOne
  @JoinColumn(name = "userid",
      nullable = false)
  @JsonIgnoreProperties(value="potluckitems",
      allowSetters = true)
  private User user;

  /**
   * Default constructor used primarily by the JPA.
   */
  public PotLuckItem() {
  }

  /**
   * Given a restaurant and payment object, create a join between them
   *
   * @param potluck The restaurant of this relationship
   * @param item    The payment of this relationship
   */
  public PotLuckItem(Potluck potluck, Item item) {
    this.potluck = potluck;
    this.item = item;
  }

  public long getPotluckitemid() {
    return potluckitemid;
  }

  public void setPotluckitemid(long id) {
    this.potluckitemid = id;
  }

  public Potluck getPotluck() {
    return potluck;
  }

  public void setPotluck(Potluck potluck) {
    this.potluck = potluck;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }
}
