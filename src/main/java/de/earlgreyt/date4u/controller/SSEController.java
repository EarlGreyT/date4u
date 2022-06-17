package de.earlgreyt.date4u.controller;

import de.earlgreyt.date4u.controller.events.ProfileUpdateEvent;
import de.earlgreyt.date4u.controller.formdata.ProfileFormData;
import de.earlgreyt.date4u.core.UnicornDetailService;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SSEController {

    private final TemplateEngine templateEngine;
    private Map<String,SseEmitter> emitterList = new HashMap<>();
    public SSEController(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    @GetMapping(path="/turbo-sse", produces= MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin
    public SseEmitter handle(Principal principal) {
        if (emitterList.get(principal.getName()) == null) {
            SseEmitter emitter = new SseEmitter();
            emitter.onCompletion(() -> emitterList.put(principal.getName(),null));
            emitter.onTimeout(() ->emitterList.put(principal.getName(),null));
            emitterList.put(principal.getName(),emitter );
        }
        return emitterList.get(principal.getName());

    }

    @EventListener
    @Async
    public void handleProfileUpdateEvent(ProfileUpdateEvent profileUpdateEvent) throws IOException {
        ProfileFormData profileFormData = profileUpdateEvent.getProfileFormData();
        for (Map.Entry<String, SseEmitter> sseEmitterEntry : emitterList.entrySet()) {
            if (sseEmitterEntry.getValue() == null){
                emitterList.put(sseEmitterEntry.getKey(), new SseEmitter());
            }
            sseEmitterEntry.getValue().send(createHtml(profileFormData));
        }
    }


    private String createHtml(ProfileFormData profileFormData){
        Context profileCard = new Context();
        profileCard.setVariable("nickname",profileFormData.getNickname());
        profileCard.setVariable("gender",profileFormData.getGender());
        profileCard.setVariable("hornlength",profileFormData.getHornlength());
        profileCard.setVariable("birthdate",profileFormData.getBirthdate());
        profileCard.setVariable("profilePhotoName",profileFormData.getProfilePhotoName());
        String profileCardString =  templateEngine.process("profile/profileCard.html",profileCard);
        String html = "<turbo-stream action=\"replace\" target=\"profCard_"+profileFormData.getNickname()+"\">"+"<template>"+profileCardString+"</template></turbo-stream>";
        html = html.replace("\n","").replace("\r","");
        return html;
    }
}
