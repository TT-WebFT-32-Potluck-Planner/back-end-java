package com.lambdaschool.foundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "items")
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long itemid;

  @Column(nullable = false)
  private String itemname;


  /**
   * Part of the join relationship between item and potluckitems
   * connects items to potluckitems
   */
  @OneToMany(mappedBy = "item",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  @JsonIgnoreProperties(value = "item",
      allowSetters = true)
  private Set<PotLuckItem> potLuckitems = new HashSet<>();

  public Item() {
  }

  public Item(String itemname, Set<PotLuckItem> potLuckitems) {
    this.itemname = itemname;
    this.potLuckitems = potLuckitems;
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

  public Set<PotLuckItem> getPotLuckitems() {
    return potLuckitems;
  }

  public void setPotLuckitems(Set<PotLuckItem> potLuckitems) {
    this.potLuckitems = potLuckitems;
  }
}
