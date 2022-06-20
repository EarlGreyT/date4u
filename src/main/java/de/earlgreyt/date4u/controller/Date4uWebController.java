package de.earlgreyt.date4u.controller;

import de.earlgreyt.date4u.core.ProfileService;
import de.earlgreyt.date4u.core.formdata.ProfileFormData;
import de.earlgreyt.date4u.core.UnicornDetailService;
import de.earlgreyt.date4u.core.UnicornDetails;
import de.earlgreyt.date4u.core.entitybeans.Profile;
import de.earlgreyt.date4u.core.formdata.SearchData;
import de.earlgreyt.date4u.repositories.ProfileRepository;
import de.earlgreyt.date4u.repositories.search.SearchCriteria;
import de.earlgreyt.date4u.repositories.search.SearchOperation;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;

import java.security.Principal;
import java.util.*;

@Controller
public class Date4uWebController {

  private final UnicornDetailService unicornDetailService;
  private final ProfileService profileService;
  private final TurboStreamBuilder turboStreamBuilder;
  public Date4uWebController(ProfileRepository profileRepository,
      UnicornDetailService unicornDetailService,
      ApplicationEventPublisher applicationEventPublisher, TemplateEngine templateEngine,
      ProfileService profileService,
      TurboStreamBuilder turboStreamBuilder) {
    this.unicornDetailService = unicornDetailService;
    this.profileService = profileService;
    this.turboStreamBuilder = turboStreamBuilder;
  }

  @RequestMapping("/*")
  public String indexPage(Model model) {
    return "index";
  }

  @RequestMapping("/matches")
  public String matchesPage(Model model, Principal principal) {
    UnicornDetails unicornDetails = (UnicornDetails) unicornDetailService.loadUserByUsername(
        principal.getName());
    Set<ProfileFormData> profileFormDataSet = profileService.profilesMatchingWith(unicornDetails);
    model.addAttribute("likedProfiles", profileFormDataSet);
    return "matches";
  }

  @RequestMapping("/matches/{nickname}/description")
  public String matchDescription(@PathVariable String nickname, Model model) {
    String matchDescription;
    model.addAttribute("nickname", nickname);
    Optional<ProfileFormData> matchProfile = profileService.findProfileByNickname(nickname);
    if (matchProfile.isPresent()) {
      model.addAttribute("description", matchProfile.get().getDescription());

    } else {
      model.addAttribute("description", "");
    }
    return "profile/descriptionBubble";
  }
  @RequestMapping("/unicorn/{nickname}")
  public String profilePage(Model model, Principal principal,@PathVariable String nickname) {
    UnicornDetails unicornDetails = (UnicornDetails) unicornDetailService.loadUserByUsername(
        principal.getName());
    ProfileFormData profileFormData = profileService.findProfileByNickname(nickname).get();
    model.addAttribute("profile", profileFormData);
    return "unicorn";
  }
  @RequestMapping("/profile")
  public String profilePage(Model model, Principal principal) {
    UnicornDetails unicornDetails = (UnicornDetails) unicornDetailService.loadUserByUsername(
        principal.getName());
    ProfileFormData profileFormData = profileService.getProfileFormData(unicornDetails);
    model.addAttribute("profile", profileFormData);
    return "profile/profile";
  }

  public String profileCardGen(Model model, ProfileFormData profileFormData) {
    model.addAttribute("profile", profileFormData);
    return "profile/profileCard";
  }

  @RequestMapping("/profile/edit")
  public String editProfilePage(Model model, Principal principal) {
    UnicornDetails unicornDetails = (UnicornDetails) unicornDetailService.loadUserByUsername(
        principal.getName());
    ProfileFormData profileFormData = profileService.getProfileFormData(unicornDetails);
    model.addAttribute("profile", profileFormData);
    return "profile/profileEdit";
  }


  private final Logger log = LoggerFactory.getLogger(getClass());

  @PostMapping("/save")
  public String saveProfile(@ModelAttribute ProfileFormData profile, Principal principal) {
    UnicornDetails unicornDetails = (UnicornDetails) unicornDetailService.loadUserByUsername(
        principal.getName());
    profileService.updateProfile(profile, unicornDetails);
    return "redirect:/profile/";
  }

  @RequestMapping("/search")
  public String searchPage(Model model) {
    SearchData searchData = new SearchData(0,0,18,200,false,false,false,false);
    model.addAttribute("searchData", searchData);
    model.addAttribute("pageNo", 0);
    return "search/search";
  }

  @PostMapping("/search")
  public String search(@ModelAttribute SearchData searchData, Principal principal, Model model) {
    List<SearchCriteria> criteriaList = new ArrayList<>();
    UnicornDetails unicornDetails = (UnicornDetails) unicornDetailService.loadUserByUsername(principal.getName());
    ProfileFormData profileFormData = profileService.getProfileFormData(unicornDetails);
    if (!profileFormData.getAttractedToGender().equals("All")){
      criteriaList.add(new SearchCriteria("gender", Profile.genderNameToGender(profileFormData.getAttractedToGender()), SearchOperation.EQUAL));
    }
    if (searchData.isConsiderMinSize()){
      criteriaList.add(new SearchCriteria("hornlength", searchData.getMinSize(), SearchOperation.GREATER_THAN_EQUAL));
    }
    if (searchData.isConsiderMaxSize()){
      criteriaList.add(new SearchCriteria("hornlength", searchData.getMaxSize(), SearchOperation.LESS_THAN_EQUAL));
    }
    if (searchData.isConsiderMinAge()){
        criteriaList.add(new SearchCriteria("birthdate", LocalDate.now().minusYears(
            searchData.getMinAge()), SearchOperation.LESS_THAN_EQUAL));
    }
    if (searchData.isConsiderMaxAge()){
      criteriaList.add(new SearchCriteria("birthdate", LocalDate.now().minusYears(
          searchData.getMaxAge()), SearchOperation.GREATER_THAN_EQUAL));
    }

    Set<ProfileFormData> matches = profileService.searchProfile(criteriaList.toArray(new SearchCriteria[0]));
    matches.removeIf(p -> p.getEmail().equals(unicornDetails.getUsername()));
    model.addAttribute("lastSearch", matches);
    model.addAttribute("searchData", searchData);
    model.addAttribute("pageNo", 1);
    return "search/search";
  }
}