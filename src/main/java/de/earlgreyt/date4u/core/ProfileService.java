package de.earlgreyt.date4u.core;


import de.earlgreyt.date4u.controller.events.LikeEvent;
import de.earlgreyt.date4u.controller.events.ProfileUpdateEvent;
import de.earlgreyt.date4u.core.entitybeans.Photo;
import de.earlgreyt.date4u.core.entitybeans.Profile;
import de.earlgreyt.date4u.core.formdata.ProfileFormData;
import de.earlgreyt.date4u.repositories.ProfileRepository;
import de.earlgreyt.date4u.repositories.search.ProfileSpec;
import de.earlgreyt.date4u.repositories.search.SearchCriteria;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

  private final ProfileRepository profileRepository;
  private final ApplicationEventPublisher applicationEventPublisher;

  public ProfileService(ProfileRepository profileRepository,
      ApplicationEventPublisher applicationEventPublisher) {
    this.profileRepository = profileRepository;
    this.applicationEventPublisher = applicationEventPublisher;
  }
  public ProfileFormData getProfileFormData(UnicornDetails unicornDetails) {
    Profile profile = unicornDetails.getProfile().get();
    return new ProfileFormData(profile);

  }

  public void addLike(String nickname, UnicornDetails unicornDetails){
    Profile unicorn = unicornDetails.getProfile().get();
    boolean[] sendEvent = {false};

    profileRepository.findProfileByNickname(nickname).ifPresent( profile -> {
      boolean added= unicorn.getProfilesILike().add(profile);
      if (profile.getProfilesILike().contains(unicorn)){
        sendEvent[0]=added;
      }
    });
    profileRepository.save(unicorn);
    if (sendEvent[0]){
      applicationEventPublisher.publishEvent(new LikeEvent(this,unicorn,profileRepository.findProfileByNickname(nickname).get()));
    }
  }

  public void removeLike(String nickname, UnicornDetails unicornDetails){
    Profile unicorn = unicornDetails.getProfile().get();
    profileRepository.findProfileByNickname(nickname).ifPresent( profile -> {
      unicorn.getProfilesILike().remove(profile);
    });
    profileRepository.save(unicorn);
  }
  public Optional<ProfileFormData> findProfileByNickname(String nickname) {
    ProfileFormData profileFormData = null;
    Optional<Profile> profile = profileRepository.findProfileByNickname(nickname);
    if (profile.isPresent()) {
      profileFormData = new ProfileFormData(profile.get());
    }

    return Optional.ofNullable(profileFormData);
  }

  public void updateProfile(ProfileFormData profileFormData, UnicornDetails unicornDetails) {
    unicornDetails.getProfile().ifPresent(profile -> {
      profile.setProfilePic(profileFormData.getProfilePhotoName());
      profile.setBirthdate(profileFormData.getBirthdate());
      profile.setDescription(profileFormData.getDescription());
      profile.setGender(profileFormData.getGender());
      profile.setHornlength(profileFormData.getHornlength());
      profile.setAttractedToGender(profileFormData.getAttractedToGender());
      profileRepository.save(profile);
      Set<String> targets = new HashSet<>();
      Set<Profile> profileSet = new HashSet<>(profile.getProfilesILike());
      profile.getProfilesThatLikeMe().forEach(
          likerProfile -> targets.add(likerProfile.getUnicorn().getEmail()));
      ProfileUpdateEvent profileUpdateEvent = new ProfileUpdateEvent(this, profileFormData, targets);
      applicationEventPublisher.publishEvent(profileUpdateEvent);
    });
  }

  public Set<ProfileFormData> profilesMatchingWith(UnicornDetails unicornDetails) {
    Set<ProfileFormData> profileFormDataSet = new HashSet<>();
    unicornDetails.getProfile().ifPresent(profile -> {
          Set<Profile> profileSet = new HashSet<>(profile.getProfilesILike());
          profileSet.retainAll(profile.getProfilesThatLikeMe());
          profileSet.forEach(profileMath -> profileFormDataSet.add(new ProfileFormData(profileMath)));
        }
    );

    return profileFormDataSet;
  }

  public Set<ProfileFormData> searchProfile(SearchCriteria... constraints){
    ProfileSpec profileSpec = new ProfileSpec();
    Set<ProfileFormData> profileFormDataList = new HashSet<>();
    for (SearchCriteria constraint : constraints) {
      profileSpec.addCriteria(constraint);
    }
    profileRepository.findAll(profileSpec).forEach(profile -> profileFormDataList.add(new ProfileFormData(profile)));
    return profileFormDataList;
  }
}

