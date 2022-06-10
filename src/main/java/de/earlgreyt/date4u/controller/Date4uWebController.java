package de.earlgreyt.date4u.controller;

import de.earlgreyt.date4u.controller.formdata.ProfileFormData;
import de.earlgreyt.date4u.core.entitybeans.Photo;
import de.earlgreyt.date4u.core.entitybeans.Profile;
import de.earlgreyt.date4u.core.repositories.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class Date4uWebController {
    private final ProfileRepository profileRepository;

    public Date4uWebController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @RequestMapping( "/**" )
    public String indexPage(Model model) {
        model.addAttribute("name", "Christian");
        model.addAttribute("numberOfProfiles",profileRepository.count());
        return "index"; }

    @RequestMapping( "/profile" )
    public String profilePage() { return "profile"; }
    @RequestMapping( "/profile/{id}" )
    public String profilePage(@PathVariable long id, Model model) {
        Optional<Profile> profile = profileRepository.findById(id);
        if (profile.isEmpty()){
            return "redirect:/";
        }
        Profile realProfile = profile.get();
        Optional<Photo> optionalPhoto = realProfile.getPhotos().stream().filter(Photo::isProfilePhoto).findFirst();
        String profilePhotoName = "";
        if (optionalPhoto.isPresent()){
            profilePhotoName = optionalPhoto.get().getName();
        }
        ProfileFormData profileFormData = new ProfileFormData(
                realProfile.getId(),
                realProfile.getNickname(),
                realProfile.getHornlength(),
                realProfile.getGender(),
                profilePhotoName
        );

        model.addAttribute("profile", profileFormData);
        return "profile";
    }

    private final Logger log = LoggerFactory.getLogger( getClass() );

    @PostMapping( "/save" )
    public String saveProfile( @ModelAttribute ProfileFormData profile ) {
        System.out.println(profile);
        Profile realProfile = profileRepository.findById(profile.getId()).get();
        realProfile.setNickname(profile.getNickname());
        realProfile.setHornlength(profile.getHornlength());
        realProfile.setGender(profile.getGender());
        realProfile.updateProfilePicture(profile.getProfilePhotoName());
        profileRepository.save(realProfile);
        return "redirect:/profile/" + profile.getId();
    }
    @RequestMapping( "/search" )
    public String searchPage(Model model) {
        model.addAttribute("profiles", profileRepository.findAll());
        return "search"; }
}