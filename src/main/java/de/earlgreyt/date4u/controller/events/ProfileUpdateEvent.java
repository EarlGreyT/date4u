package de.earlgreyt.date4u.controller.events;

import de.earlgreyt.date4u.core.formdata.ProfileFormData;
import java.util.Set;
import org.springframework.context.ApplicationEvent;

public class ProfileUpdateEvent extends ApplicationEvent {
    private ProfileFormData profileFormData;
    private Set<String> targets;
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
