package de.earlgreyt.date4u.controller.formdata;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProfileFormData {

    private long id;
    private String nickname;

    private int hornlength;
    private int gender;
    private String profilePhotoName;


    public ProfileFormData() {
    }

    public ProfileFormData(long id, String nickname,
                            int hornlength, int gender, String profilePhotoName) {
        this.id = id;
        this.nickname = nickname;
        this.hornlength = hornlength;
        this.gender = gender;
        this.profilePhotoName = profilePhotoName;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setId(long id) {
        this.id = id;
    }
    // + Setter + Getter + toString()

    @Override
    public String toString() {
        return "ProfileFormData{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", hornlength=" + hornlength +
                ", gender=" + gender +
                '}';
    }
}