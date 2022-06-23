package de.earlgreyt.date4u.core.events;

import de.earlgreyt.date4u.core.entitybeans.Profile;
import org.springframework.context.ApplicationEvent;

public class LikeEvent extends ApplicationEvent {
  private final Profile liker;
  private final Profile likee;
  public LikeEvent(Object source, Profile liker, Profile likee) {
    super(source);
    this.liker = liker;
    this.likee = likee;
  }

  public Profile getLiker() {
    return liker;
  }

  public Profile getLikee() {
    return likee;
  }
}
