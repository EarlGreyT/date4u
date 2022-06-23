package de.earlgreyt.date4u.core.events;

import de.earlgreyt.date4u.core.ProfileService;
import de.earlgreyt.date4u.core.entitybeans.Profile;
import java.util.Set;
import org.springframework.context.ApplicationEvent;

public class DisLikeEvent extends ApplicationEvent {
  private final Profile disliker;
  private final Profile target;
  public DisLikeEvent(ProfileService profileService, Profile disliker,
      Profile target) {
    super(profileService);
    this.disliker = disliker;
    this.target = target;
  }

  public Profile getDisliker() {
    return disliker;
  }

  public Profile getTarget() {
    return target;
  }

  @Override
  public String toString() {
    return "DisLikeEvent{" +
        "disliker=" + disliker +
        ", targets=" + target +
        '}';
  }
}
