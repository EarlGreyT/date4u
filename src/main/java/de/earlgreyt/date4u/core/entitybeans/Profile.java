package de.earlgreyt.date4u.core.entitybeans;

import javax.persistence.CascadeType;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Entity()
@Access(AccessType.FIELD)
public class Profile {

    public static final int FEE = 1;
    public static final int MAA = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private LocalDate birthdate;
    private short hornlength;
    private byte gender;

    @Column(name = "attracted_to_gender")
    private Byte attractedToGender;

    private String description;
    private LocalDateTime lastseen;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Unicorn unicorn;

    @OneToMany(mappedBy = "profile", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Photo> photos;


    protected Profile() {
    }

    public void addPhoto(Photo p) {
        p.setProfile(this);
        photos.add(p);
    }

    public Profile(String nickname, LocalDate birthdate, int hornlength, int gender, Integer attractedToGender, String description, LocalDateTime lastseen) {
        setNickname(nickname);
        setBirthdate(birthdate);
        setHornlength(hornlength);
        setGender(gender);
        setAttractedToGender(attractedToGender);
        setDescription(description);
        setLastseen(lastseen);
    }

    public void updateProfilePicture(String photoName) {
        if (photoName!=null && !photoName.equals("")) {
            photos.forEach(photo -> photo.setProfilePhoto(photo.getName().equals(photoName)));
        }
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public int getHornlength() {
        return hornlength;
    }

    public void setHornlength(int hornlength) {
        this.hornlength = (short) hornlength;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = (byte) gender;
    }

    public @Nullable Integer getAttractedToGender() {
        return attractedToGender == null ? null : attractedToGender.intValue();
    }

    public void setAttractedToGender(@Nullable Integer attractedToGender) {
        this.attractedToGender = attractedToGender == null ? null : attractedToGender.byteValue();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLastseen() {
        return lastseen;
    }

    public void setLastseen(LocalDateTime lastseen) {
        this.lastseen = lastseen;
    }

    public void setHornlength(short hornlength) {
        this.hornlength = hornlength;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public void setAttractedToGender(Byte attractedToGender) {
        this.attractedToGender = attractedToGender;
    }

    public Unicorn getUnicorn() {
        return unicorn;
    }

    public void setUnicorn(Unicorn unicorn) {
        this.unicorn = unicorn;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public Optional<Photo> getProfilePic() {
        return photos.stream().filter(Photo::isProfilePhoto).findAny();
    }

    public void setProfilePic(String profilePicName) {
        Optional<Photo> newProfilePic = photos.stream().filter(photo-> photo.getName().equals(profilePicName)).findAny();
        newProfilePic.ifPresent( profilePhoto ->
                {
                this.getProfilePic().ifPresent(photo -> photo.setProfilePhoto(false));
                profilePhoto.setProfilePhoto(true);
        });
    }
    public void setGender(String genderName){
        switch (genderName){
            case "Male" -> setGender(1);
            case "Female" -> setGender(2);
            default -> setGender(0);
        }
    }
    public String getGenderName(){
        switch (gender){
            case 1 -> {
                return "Male";
            }
            case 2 ->{
                return "Female";
            }
            default -> {
                return "Non-Binary";
            }
        }
    }
    @Override
    public boolean equals(Object o) {
        return o instanceof Profile profile && nickname.equals(profile.nickname);
    }

    @Override
    public int hashCode() {
        return nickname.hashCode();
    }

    @Override
    public String toString() {
        return "Profile[id=%d]".formatted(id);
    }
}