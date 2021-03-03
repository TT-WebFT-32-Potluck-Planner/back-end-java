package com.lambdaschool.foundation.models;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AttendeeId implements Serializable {

  private long attendee;

  private long potluck;

  public AttendeeId() {
  }

  public long getAttendee() {
    return attendee;
  }

  public void setAttendee(long attendee) {
    this.attendee = attendee;
  }

  public long getPotluck() {
    return potluck;
  }

  public void setPotluck(long potluck) {
    this.potluck = potluck;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    AttendeeId that = (AttendeeId) o;
    return attendee == that.attendee &&
        potluck == that.potluck;
  }

  @Override
  public int hashCode()
  {
    return 37;
  }

}
