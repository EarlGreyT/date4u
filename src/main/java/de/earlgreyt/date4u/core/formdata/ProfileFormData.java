package de.earlgreyt.date4u.core.formdata;

import de.earlgreyt.date4u.core.entitybeans.Photo;
import de.earlgreyt.date4u.core.entitybeans.Profile;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class ProfileFormData {


    private String nickname;

    private int hornlength;
    private String gender;
    private String profilePhotoName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    private String description;
    private List<String> photos = new ArrayList<>();
    private String email;
    private String attractedToGender;
    public ProfileFormData() {
        this.nickname = null;
        this.hornlength = -1;
        this.gender = null;
        this.profilePhotoName = null;
        this.birthdate = null;
        this.description = null;
        this.email = null;
        this.attractedToGender = null;

    }

    public ProfileFormData(Profile profile) {
        this(profile.getNickname(), profile.getHornlength(), genderToGenderName(profile.getGender()), profile.getProfilePic(), profile.getBirthdate(), profile.getDescription(), profile.getPhotos(), profile.getUnicorn().getEmail(), genderToGenderName(profile.getAttractedToGender()));
    }

    public ProfileFormData(String nickname, int hornlength, String gender, Optional<Photo> profilePhoto, LocalDate birthdate, String description, List<Photo> photos, String email, String attractedToGender) {
        this.nickname = nickname;
        this.hornlength = hornlength;
        this.gender = gender;
        if (profilePhoto.isPresent()) {
            this.profilePhotoName = profilePhoto.get().getName();
        } else {
            this.profilePhotoName ="__noPhoto";
        }
        this.birthdate = birthdate;
        this.description = description;
        this.attractedToGender = attractedToGender;
        this.email = email;
        for (Photo photo : photos) {
            this.photos.add(photo.getName());
        }
    }

    public String getProfilePhotoName() {
        return profilePhotoName;
    }

    public void setProfilePhotoName(String profilePhotoName) {
        this.profilePhotoName = profilePhotoName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getHornlength() {
        return hornlength;
    }

    public void setHornlength(int hornlength) {
        this.hornlength = hornlength;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
    public void setBirthdate(String birthdate){ this.birthdate = LocalDate.parse(birthdate);}
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setAttractedToGender(String genderName){
        this.attractedToGender = genderName;
    }
    public String getAttractedToGender() {
        return attractedToGender;
    }

    @Override
    public String toString() {
        return "ProfileFormData{" + "nickname='" + nickname + '\'' + ", hornlength=" + hornlength + ", gender=" + gender + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileFormData)) {
            return false;
        }
        ProfileFormData that = (ProfileFormData) o;
        return getHornlength() == that.getHornlength() && getNickname().equals(that.getNickname())
            && getGender().equals(that.getGender()) && getProfilePhotoName().equals(
            that.getProfilePhotoName()) && getBirthdate().equals(that.getBirthdate())
            && getDescription().equals(that.getDescription()) && getPhotos().equals(
            that.getPhotos())
            && getEmail().equals(that.getEmail()) && getAttractedToGender().equals(
            that.getAttractedToGender());
    }






    public static byte genderNameToGender(String genderName) {
        switch (genderName) {
            case "Female" -> {
                return 1;
            }
            case "Male" -> {
                return 2;
            }
            case "Non-Binary" -> {
                return 0;
            }
            default -> {
                return -1;
            }
        }
    }

    public static String genderToGenderName(int gender) {
        switch (gender) {
            case 1 -> {
                return "Female";
            }
            case 2 -> {
                return "Male";
            }
            case 0 ->{
                return "Non-Binary";
            }
            default -> {
                return "All";
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNickname(), getHornlength(), getGender(), getProfilePhotoName(),
            getBirthdate(), getDescription(), getPhotos(), getEmail(), getAttractedToGender());
    }
}