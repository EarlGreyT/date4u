package de.earlgreyt.date4u.core;


import de.earlgreyt.date4u.core.entitybeans.Photo;
import de.earlgreyt.date4u.core.events.DisLikeEvent;
import de.earlgreyt.date4u.core.events.LikeEvent;
import de.earlgreyt.date4u.core.events.ProfileUpdateEvent;
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

  public boolean userLikesUnicorn(UnicornDetails unicornDetails, String nickname) {
    Optional<Profile> unicorn = unicornDetails.getProfile();
    if (unicorn.isPresent()) {
      Profile profile = unicorn.get();
      return profile.getProfilesILike().stream().anyMatch(p -> p.getNickname().equals(nickname));
    }
    return false;
  }

  public void addLike(String nickname, UnicornDetails unicornDetails) {
    Profile unicorn = unicornDetails.getProfile().get();
    boolean[] sendEvent = {false};

    profileRepository.findProfileByNickname(nickname).ifPresent(profile -> {
      boolean added = unicorn.getProfilesILike().add(profile);
      if (profile.getProfilesILike().contains(unicorn) && added) {
        applicationEventPublisher.publishEvent(new LikeEvent(this, unicorn, profile));
      }
    });
    profileRepository.save(unicorn);

  }

  public void removeLike(String nickname, UnicornDetails unicornDetails) {
    Profile unicorn = unicornDetails.getProfile().get();
    profileRepository.findProfileByNickname(nickname).ifPresent(profile -> {
      unicorn.getProfilesILike().remove(profile);
      applicationEventPublisher.publishEvent(new DisLikeEvent(this, unicorn, profile));
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
      ProfileUpdateEvent profileUpdateEvent = new ProfileUpdateEvent(this, profileFormData,
          targets);
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

  public Set<ProfileFormData> searchProfile(SearchCriteria... constraints) {
    ProfileSpec profileSpec = new ProfileSpec();
    Set<ProfileFormData> profileFormDataList = new HashSet<>();
    for (SearchCriteria constraint : constraints) {
      profileSpec.addCriteria(constraint);
    }
    profileRepository.findAll(profileSpec)
        .forEach(profile -> profileFormDataList.add(new ProfileFormData(profile)));
    return profileFormDataList;
  }

  public void addPhoto(UnicornDetails unicornDetails, String filename) {
    unicornDetails.getProfile().ifPresent(profile -> {
      Photo photo = new Photo(filename);
      photo.setName(filename);
      photo.setProfile(profile);
      photo.setProfilePhoto(false);
      profile.addPhoto(photo);
      profileRepository.save(profile);
    });
  }
}

