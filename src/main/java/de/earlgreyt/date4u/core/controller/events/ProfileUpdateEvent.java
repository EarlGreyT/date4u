package de.earlgreyt.date4u.core.controller.events;

import de.earlgreyt.date4u.core.controller.formdata.ProfileFormData;
import org.springframework.context.ApplicationEvent;

public class ProfileUpdateEvent extends ApplicationEvent {
    private ProfileFormData profileFormData;

    public ProfileUpdateEvent(Object source, ProfileFormData profileFormData) {
        super(source);
        this.profileFormData = profileFormData;
    }
    public ProfileFormData getProfileFormData() {
        return profileFormData;
    }
}
