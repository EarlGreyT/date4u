package de.earlgreyt.date4u.controller.events;

import de.earlgreyt.date4u.controller.formdata.ProfileFormData;
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
