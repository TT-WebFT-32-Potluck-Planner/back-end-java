package com.lambdaschool.foundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The entity allowing interaction with the attendees table
 * <p>
 * requires each combination of attendee and potluck to be unique. The same attendee id cannot be assigned to the same user more than once.
 */
@Entity
@Table(name = "attendees")
@IdClass(AttendeeId.class)
public class Attendee extends Auditable implements Serializable {

  /**
   * The userid of the user assigned to this attendeeid is what is stored in the database.
   * This is the entire user object!
   * <p>
   * Forms a Many to One relationship between attendees and users.
   * A user can have many attendeeids.
   */

  @Id
  @ManyToOne
  @JoinColumn(name = "userid")
  @JsonIgnoreProperties(value={"potlucks","potluck","items","attendees", "user", "roles", "potlucksattending"},
    allowSetters = true)
  private User attendee;

  /**
   * The potluckid of the potluck attached to this attendeeid is what is stored in the database.
   * This is the entire potluck object!
   * <p>
   * Forms a Many to One relationship between attendees and potlucks.
   * A potluck can have many ateendeeids.
   */

  @Id
  @ManyToOne
  @JoinColumn(name = "potluckid")
  @JsonIgnoreProperties(value= {"attendees", "attending", "items"},
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
   * @param attendee      the user (User) assigned to the attendee
   * @param potluck   the potluck (Potluck) attached the attendee
   */
  public Attendee(User attendee, Potluck potluck) {
    this.attendee = attendee;
    this.potluck = potluck;
  }

  /**
   * Getter for user
   *
   * @return the user object associated with this attendee.
   */
  public User getAttendee() {
    return attendee;
  }

  /**
   * Setter for user
   *
   * @param user the user object to replace the one currently assigned to this attendee object
   */
  public void setAttendee(User user) {
    this.attendee = user;
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

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof UserRoles))
    {
      return false;
    }
    Attendee that = (Attendee) o;
    return ((attendee == null) ? 0 : attendee.getUserid()) == ((that.attendee == null) ? 0 : that.attendee.getUserid()) &&
        ((potluck == null) ? 0 : potluck.getPotluckid()) == ((that.potluck == null) ? 0 : that.potluck.getPotluckid());
  }

  @Override
  public int hashCode()
  {
    // return Objects.hash(user.getUserid(), role.getRoleid());
    return 37;
  }

}
