package de.earlgreyt.date4u.controller;

import de.earlgreyt.date4u.controller.formdata.ProfileFormData;
import de.earlgreyt.date4u.core.UnicornDetailService;
import de.earlgreyt.date4u.core.UnicornDetails;
import de.earlgreyt.date4u.core.entitybeans.Photo;
import de.earlgreyt.date4u.core.entitybeans.Profile;
import de.earlgreyt.date4u.core.repositories.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
public class Date4uWebController {
    private final ProfileRepository profileRepository;
    private final UnicornDetailService unicornDetailService;
    public Date4uWebController(ProfileRepository profileRepository, UnicornDetailService unicornDetailService) {
        this.profileRepository = profileRepository;
        this.unicornDetailService = unicornDetailService;
    }

    @RequestMapping( "/*" )
    public String indexPage(Model model) {
        return "index"; }

    @RequestMapping("/matches")
    public String matchesPage(Model model, Principal principal){
        UnicornDetails unicornDetails = (UnicornDetails) unicornDetailService.loadUserByUsername(principal.getName());
        Profile profile = unicornDetails.getProfile().get();
        Set<Profile> matchSet = profile.getProfilesILike();
        Set<Profile> profilesThatLikePrincipal = profile.getProfilesThatLikeMe();
        Set<ProfileFormData> profileFormDataSet = new HashSet<>();
        matchSet.retainAll(profilesThatLikePrincipal);
        for (Profile likedProfile : matchSet) {
            profileFormDataSet.add(new ProfileFormData(likedProfile));
        }
        model.addAttribute("likedProfiles", profileFormDataSet);
        return "matches";
    }

    @RequestMapping("/matches/{nickname}/description")
    public String matchDescription(@PathVariable String nickname, Model model){
        String matchDescription;
        model.addAttribute("nickname", nickname);
        Optional<Profile> matchProfile = profileRepository.findProfileByNickname(nickname);
        if (matchProfile.isPresent()){
            model.addAttribute("description", matchProfile.get().getDescription());

        } else {
            model.addAttribute("description","");
        }
        return "profile/descriptionBubble";
    }
    @RequestMapping( "/profile" )
    public String profilePage(Model model, Principal principal) {
        ProfileFormData profileFormData = getProfileFormDataFromPrinciple(principal);
        model.addAttribute("profile", profileFormData);
        return "profile/profile";
    }
    @RequestMapping("/profile/edit")
    public String editProfilePage(Model model, Principal principal){
        ProfileFormData profileFormData = getProfileFormDataFromPrinciple(principal);
        model.addAttribute("profile", profileFormData);
        return "profile/profileEdit";
    }

    private ProfileFormData getProfileFormDataFromPrinciple(Principal principal) {
        UnicornDetails unicornDetails = (UnicornDetails) unicornDetailService.loadUserByUsername(principal.getName());
        Profile profile = unicornDetails.getProfile().get();
        Optional<Photo> optionalPhoto = profile.getProfilePic();
        String profilePhotoName = "";
        if (optionalPhoto.isPresent()){
            profilePhotoName = optionalPhoto.get().getName();
        }
        return new ProfileFormData(
                profile.getId(),
                profile.getNickname(),
                profile.getHornlength(),
                profile.getGenderName(),
                profilePhotoName,
                profile.getBirthdate(),
                profile.getDescription(),
                profile.getPhotos()
        );

    }

    private final Logger log = LoggerFactory.getLogger( getClass() );

    @PostMapping( "/save" )
    public String saveProfile( @ModelAttribute ProfileFormData profile, Principal principal) {
        Profile realProfile = profileRepository.findById(profile.getId()).get();
        UnicornDetails unicornDetails = (UnicornDetails) unicornDetailService.loadUserByUsername(principal.getName());
            unicornDetails.getProfile().ifPresent( unicornProfile ->{
                if (unicornProfile.getId() == realProfile.getId()) {
                    realProfile.setNickname(profile.getNickname());
                    realProfile.setHornlength(profile.getHornlength());
                    realProfile.setGender(profile.getGender());
                    realProfile.updateProfilePicture(profile.getProfilePhotoName());
                    realProfile.setBirthdate(profile.getBirthdate());
                    realProfile.setDescription(profile.getDescription());
                    profileRepository.save(realProfile);
                }
            });

        return "redirect:/profile/";
    }
    @RequestMapping( "/search" )
    public String searchPage(Model model) {
        model.addAttribute("profiles", profileRepository.findAll());
        return "search"; }


}