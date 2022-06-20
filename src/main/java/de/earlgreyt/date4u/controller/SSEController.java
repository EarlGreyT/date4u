package de.earlgreyt.date4u.controller;

import de.earlgreyt.date4u.controller.events.ProfileUpdateEvent;
import de.earlgreyt.date4u.core.formdata.ProfileFormData;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SSEController {

    private final TurboStreamBuilder turboStreamBuilder;
    private Map<String,SseEmitter> emitterList = new HashMap<>();

    public SSEController(TurboStreamBuilder turboStreamBuilder) {
        this.turboStreamBuilder = turboStreamBuilder;
    }

    @GetMapping(path="/turbo-sse", produces= MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin
    public SseEmitter handle(Principal principal) {
        if (emitterList.get(principal.getName()) == null) {
            SseEmitter emitter = new SseEmitter();
            emitter.onCompletion(() -> emitterList.remove(principal.getName())); //is this necessary?
            emitter.onTimeout(() -> emitterList.remove(principal.getName()));
            emitterList.put(principal.getName(),emitter );
        }
        return emitterList.get(principal.getName());

    }

    @EventListener
    @Async
    public void handleProfileUpdateEvent(ProfileUpdateEvent profileUpdateEvent) throws IOException {
        ProfileFormData profileFormData = profileUpdateEvent.getProfileFormData();
        for (String target :profileUpdateEvent.getTargets()) {
            SseEmitter sseEmitter=emitterList.get(target);
            if (sseEmitter != null) {
                sseEmitter.send(createProfileCardHtml(profileFormData));
            }
        }
    }


    private String createProfileCardHtml(ProfileFormData profileFormData){
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("nickname", profileFormData.getNickname());
        objectMap.put("gender",profileFormData.getGender());
        objectMap.put("hornlength",profileFormData.getHornlength());
        objectMap.put("birthdate",profileFormData.getBirthdate());
        objectMap.put("profilePhotoName",profileFormData.getProfilePhotoName());
        objectMap.put("attractedToGender", profileFormData.getAttractedToGender());
        return turboStreamBuilder.buildTurboStream("replace","profCard_"+profileFormData.getNickname(),"profile/profileCard.html",objectMap)+turboStreamBuilder.buildTurboStream("replace","unicor_"+profileFormData.getNickname(),"profile/unicornProfile.html",objectMap);
    }
}
