package de.earlgreyt.date4u.core.events;

import de.earlgreyt.date4u.core.formdata.ProfileFormData;
import java.util.Set;
import org.springframework.context.ApplicationEvent;

public class ProfileUpdateEvent extends ApplicationEvent {
    private final ProfileFormData profileFormData;
    private final Set<String> targets;
    public ProfileUpdateEvent(Object source, ProfileFormData profileFormData, Set<String> targets) {
        super(source);
        this.profileFormData = profileFormData;
        this.targets = targets;
    }
    public ProfileFormData getProfileFormData() {
        return profileFormData;
    }

    public Set<String> getTargets() {
        return targets;
    }
}
