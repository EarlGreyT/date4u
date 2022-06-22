package de.earlgreyt.date4u.controller;

import de.earlgreyt.date4u.core.ProfileService;
import de.earlgreyt.date4u.core.RegisterService;
import de.earlgreyt.date4u.core.TurboStreamBuilder;
import de.earlgreyt.date4u.core.exceptions.EmailAlreadyInUseException;
import de.earlgreyt.date4u.core.formdata.ProfileFormData;
import de.earlgreyt.date4u.core.UnicornDetailService;
import de.earlgreyt.date4u.core.UnicornDetails;
import de.earlgreyt.date4u.core.entitybeans.Profile;
import de.earlgreyt.date4u.core.formdata.SearchData;
import de.earlgreyt.date4u.core.formdata.UserDTO;
import de.earlgreyt.date4u.repositories.search.SearchCriteria;
import de.earlgreyt.date4u.repositories.search.SearchOperation;
import java.time.LocalDate;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Validated
public class Date4uWebController {

  private final UnicornDetailService unicornDetailService;
  private final ProfileService profileService;
  private final TurboStreamBuilder turboStreamBuilder;
  private final RegisterService registerService;

  public Date4uWebController(
      UnicornDetailService unicornDetailService,
      ProfileService profileService,
      TurboStreamBuilder turboStreamBuilder,
      RegisterService registerService) {
    this.unicornDetailService = unicornDetailService;
    this.profileService = profileService;
    this.turboStreamBuilder = turboStreamBuilder;
    this.registerService = registerService;
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
    model.addAttribute("nickname", nickname);
    Optional<ProfileFormData> matchProfile = profileService.findProfileByNickname(nickname);
    if (matchProfile.isPresent()) {
      model.addAttribute("description", matchProfile.get().getDescription());

    } else {
      model.addAttribute("description", "");
    }
    return "profile/descriptionBubble";
  }

  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    model.addAttribute("user", new UserDTO());
    model.addAttribute("errors", new HashMap<String, String>());
    return "register";
  }

  @PostMapping("/register")
  public String processSignUp(@ModelAttribute UserDTO user, Model model) {
    model.addAttribute("user", user);
    HashMap<String, String> errorMap = new HashMap<>();
    try {
      registerService.register(user);
    } catch (EmailAlreadyInUseException e) {
      errorMap.put("email", "Email already in Use");
      return "register";
    } catch (ConstraintViolationException e) {
      for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
        if (constraintViolation.getPropertyPath().toString().contains("nickname")) {
          errorMap.put("nickname", constraintViolation.getMessage());
        } else if(constraintViolation.getPropertyPath().toString().contains("email")){
          errorMap.put("nickname", constraintViolation.getMessage());
        } else {
          errorMap.put("password", constraintViolation.getMessage());
        }
      }
      model.addAttribute("errors", errorMap);
      return "register";
    }
    return "redirect:/login";


  }

  @RequestMapping("/unicorn/{nickname}")
  public String profilePage(Model model, Principal principal, @PathVariable String nickname) {
    UnicornDetails unicornDetails = (UnicornDetails) unicornDetailService.loadUserByUsername(
        principal.getName());
    ProfileFormData profileFormData = profileService.findProfileByNickname(nickname).get();
    model.addAttribute("profile", profileFormData);
    model.addAttribute("likes", unicornDetails.getProfile().get().getProfilesILike().stream()
        .anyMatch(profile -> profile.getNickname().equals(nickname)));
    return "unicorn";
  }

  @PostMapping("/unicorn/{nickname}")
  public String likeProfilePage(Model model, Principal principal, @PathVariable String nickname) {
    UnicornDetails unicornDetails = (UnicornDetails) unicornDetailService.loadUserByUsername(
        principal.getName());
    profileService.addLike(nickname, unicornDetails);
    return "redirect:/unicorn/" + nickname;
  }

  @PostMapping("/unicorn/{nickname}/dislike")
  public String dislikeProfilePage(Model model, Principal principal,
      @PathVariable String nickname) {
    UnicornDetails unicornDetails = (UnicornDetails) unicornDetailService.loadUserByUsername(
        principal.getName());
    profileService.removeLike(nickname, unicornDetails);
    return "redirect:/unicorn/" + nickname;
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
  public String searchPage(Model model, Principal principal) {
    ProfileFormData profile = profileService.getProfileFormData(
        (UnicornDetails) unicornDetailService.loadUserByUsername(principal.getName()));
    SearchData searchData = new SearchData(0, 0, 18, 200, false, false, false, false);
    model.addAttribute("searchData", searchData);
    model.addAttribute("pageNo", 0);
    model.addAttribute("profile", profile);
    return "search/search";
  }

  @PostMapping("/search")
  public String search(@ModelAttribute SearchData searchData, Principal principal, Model model) {
    List<SearchCriteria> criteriaList = new ArrayList<>();
    UnicornDetails unicornDetails = (UnicornDetails) unicornDetailService.loadUserByUsername(
        principal.getName());
    ProfileFormData profileFormData = profileService.getProfileFormData(unicornDetails);
    if (!profileFormData.getAttractedToGender().equals("All")) {
      criteriaList.add(new SearchCriteria("gender",
          Profile.genderNameToGender(profileFormData.getAttractedToGender()),
          SearchOperation.EQUAL));
    }
    if (searchData.isConsiderMinSize()) {
      criteriaList.add(new SearchCriteria("hornlength", searchData.getMinSize(),
          SearchOperation.GREATER_THAN_EQUAL));
    }
    if (searchData.isConsiderMaxSize()) {
      criteriaList.add(new SearchCriteria("hornlength", searchData.getMaxSize(),
          SearchOperation.LESS_THAN_EQUAL));
    }
    if (searchData.isConsiderMinAge()) {
      criteriaList.add(new SearchCriteria("birthdate",
          searchData.getMinAge(),
          SearchOperation.NOT_BEFORE_NOW)); //Time Travelers from the future are fair game, no way to tell their age
    }
    if (searchData.isConsiderMaxAge()) {
      criteriaList.add(new SearchCriteria("birthdate",
          searchData.getMaxAge(), SearchOperation.BEFORE_NOW));
    }
    System.out.println("STUPID MIN AGE! " + LocalDate.now().minusYears(
        searchData.getMinAge()));
    System.out.println("STUPID MAX AGE! " + LocalDate.now().minusYears(
        searchData.getMaxAge()));
    Set<ProfileFormData> matches = profileService.searchProfile(
        criteriaList.toArray(new SearchCriteria[0]));
    matches.removeIf(p -> p.getEmail().equals(unicornDetails.getUsername()));
    model.addAttribute("lastSearch", matches);
    model.addAttribute("searchData", searchData);
    model.addAttribute("pageNo", 1);
    model.addAttribute("profile", profileFormData);
    return "search/search";
  }
}