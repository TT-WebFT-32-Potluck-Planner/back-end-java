package com.lambdaschool.foundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * The entity allowing interaction with the attendees table
 * <p>
 * requires each combination of attendee and potluck to be unique. The same attendee id cannot be assigned to the same user more than once.
 */
@Entity
@Table(name = "attendees")
public class Attendee extends Auditable {

  /**
   * The primary key (long) of the attendees table
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long attendeeid;

  /**
   * The userid of the user assigned to this attendeeid is what is stored in the database.
   * This is the entire user object!
   * <p>
   * Forms a Many to One relationship between attendees and users.
   * A user can have many attendeeids.
   */
  @ManyToOne
  @JoinColumn(name = "userid",
    nullable = false)
  @JsonIgnoreProperties(value="attendees",
    allowSetters = true)
  private User user;

  /**
   * The potluckid of the potluck attached to this attendeeid is what is stored in the database.
   * This is the entire potluck object!
   * <p>
   * Forms a Many to One relationship between attendees and potlucks.
   * A potluck can have many ateendeeids.
   */
  @ManyToOne
  @JoinColumn(name = "potluckid",
      nullable = false)
  @JsonIgnoreProperties(value="attendees",
      allowSetters = true)
  private Potluck potluck;

  /**
   * The default controller is required by JPA
   */
  public Attendee() {
  }

  /**
   * Given the parameters, create a new attendeeid object
   *
   * @param user      the user (User) assigned to the attendee
   * @param potluck   the potluck (Potluck) attached the attendee
   */
  public Attendee(User user, Potluck potluck) {
    this.user = user;
    this.potluck = potluck;
  }

  /**
   * Getter for attendeeid
   *
   * @return the primary key (long) of this attendee object
   */
  public long getAttendeeid() {
    return attendeeid;
  }

  /**
   * Setter for attendeeid. Used for seeding data
   *
   * @param attendeeid the new primary key (long) of this attendee object
   */
  public void setAttendeeid(long attendeeid) {
    this.attendeeid = attendeeid;
  }

  /**
   * Getter for user
   *
   * @return the user object associated with this attendee.
   */
  public User getUser() {
    return user;
  }

  /**
   * Setter for user
   *
   * @param user the user object to replace the one currently assigned to this attendee object
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Getter for potluck
   *
   * @return the potluck object associated with this attendee.
   */
  public Potluck getPotluck() {
    return potluck;
  }

  /**
   * Setter for potluck
   *
   * @param potluck the potluck object to replace the one currently assigned to this attendee object
   */
  public void setPotluck(Potluck potluck) {
    this.potluck = potluck;
  }

}
