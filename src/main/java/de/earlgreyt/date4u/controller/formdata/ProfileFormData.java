package de.earlgreyt.date4u.controller.formdata;

import de.earlgreyt.date4u.core.entitybeans.Photo;
import de.earlgreyt.date4u.core.entitybeans.Profile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class ProfileFormData {

    private long id;
    private String nickname;

    private int hornlength;
    private String gender;
    private String profilePhotoName;
    private LocalDate birthdate;
    private String description;
    private LinkedList<String> photos = new LinkedList<>();

    public ProfileFormData() {
    }

    public ProfileFormData(Profile profile) {
        this(profile.getId(), profile.getNickname(), profile.getHornlength(), profile.getGenderName(), profile.getProfilePic().get().getName(), profile.getBirthdate(), profile.getDescription(), profile.getPhotos());
    }

    public ProfileFormData(long id, String nickname, int hornlength, String gender, String profilePhotoName, LocalDate birthdate, String description, List<Photo> photos) {
        this.id = id;
        this.nickname = nickname;
        this.hornlength = hornlength;
        this.gender = gender;
        this.profilePhotoName = profilePhotoName;
        this.birthdate = birthdate;
        this.description = description;
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

    public long getId() {
        return id;
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

    public void setId(long id) {
        this.id = id;
    }
    // + Setter + Getter + toString()

    public LinkedList<String> getPhotos() {
        return photos;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ProfileFormData{" + "id=" + id + ", nickname='" + nickname + '\'' + ", hornlength=" + hornlength + ", gender=" + gender + '}';
    }

}