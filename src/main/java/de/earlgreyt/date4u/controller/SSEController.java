package de.earlgreyt.date4u.controller;

import de.earlgreyt.date4u.core.TurboStreamBuilder;
import de.earlgreyt.date4u.core.events.LikeEvent;
import de.earlgreyt.date4u.core.events.ProfileUpdateEvent;
import de.earlgreyt.date4u.core.entitybeans.Profile;
import de.earlgreyt.date4u.core.formdata.ProfileFormData;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SSEController {

  private final TurboStreamBuilder turboStreamBuilder;
  private Map<String, SseEmitter> emitterList = new ConcurrentHashMap<>();

  public SSEController(TurboStreamBuilder turboStreamBuilder) {
    this.turboStreamBuilder = turboStreamBuilder;
  }


  @GetMapping(path = "/turbo-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter handle(Principal principal) {
    if (emitterList.get(principal.getName()) == null) {
      SseEmitter emitter = new SseEmitter(-1L); //super ugly but it works, no more 503s
      emitter.onCompletion(() -> emitterList.remove(principal.getName()));
      emitter.onTimeout(() -> emitterList.remove(principal.getName()));//is this necessary?
      emitterList.put(principal.getName(), emitter);
    }
    return emitterList.get(principal.getName());

  }

  @EventListener
  @Async
  public void handleProfileUpdateEvent(ProfileUpdateEvent profileUpdateEvent) throws IOException {
    ProfileFormData profileFormData = profileUpdateEvent.getProfileFormData();
    for (String target : profileUpdateEvent.getTargets()) {
      SseEmitter sseEmitter = emitterList.get(target);
      if (sseEmitter != null) {
        sseEmitter.send(createProfileCardHtml(profileFormData));
        System.out.println("STUPID UPDATE EVENT FOR " + target);
      }

    }
  }

  @EventListener
  @Async
  public void handleLikeEvent(LikeEvent likeEvent) throws IOException {
    Profile liker = likeEvent.getLiker();
    Profile likee = likeEvent.getLikee();
    SseEmitter likerEmitter = emitterList.get(liker.getUnicorn().getEmail());
    SseEmitter likeeEmitter = emitterList.get(likee.getUnicorn().getEmail());
    if (likeeEmitter != null && likeeEmitter != null) {
      likerEmitter.send(appendProfileCardHtml(new ProfileFormData(likee)));
      likeeEmitter.send(appendProfileCardHtml(new ProfileFormData(liker)));
    }
  }

  private String appendProfileCardHtml(ProfileFormData profileFormData) {
    Map<String, Object> objectMap = new HashMap<>();
    objectMap.put("nickname", profileFormData.getNickname());
    objectMap.put("gender", profileFormData.getGender());
    objectMap.put("hornlength", profileFormData.getHornlength());
    objectMap.put("birthdate", profileFormData.getBirthdate());
    objectMap.put("profilePhotoName", profileFormData.getProfilePhotoName());
    objectMap.put("attractedToGender", profileFormData.getAttractedToGender());
    return turboStreamBuilder.buildTurboStream("append", "matchesList", "profile/profileCard.html",
        objectMap);
  }

  private String createProfileCardHtml(ProfileFormData profileFormData) {
    Map<String, Object> objectMap = new HashMap<>();
    objectMap.put("nickname", profileFormData.getNickname());
    objectMap.put("gender", profileFormData.getGender());
    objectMap.put("hornlength", profileFormData.getHornlength());
    objectMap.put("birthdate", profileFormData.getBirthdate());
    objectMap.put("profilePhotoName", profileFormData.getProfilePhotoName());
    objectMap.put("attractedToGender", profileFormData.getAttractedToGender());
    return
        turboStreamBuilder.buildTurboStream("replace", "profCard_" + profileFormData.getNickname(),
            "profile/profileCard.html", objectMap) + turboStreamBuilder.buildTurboStream("replace",
            "unicorn_" + profileFormData.getNickname(), "profile/unicornProfile.html", objectMap);
  }
}
