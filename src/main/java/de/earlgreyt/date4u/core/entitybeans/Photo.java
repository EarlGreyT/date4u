package de.earlgreyt.date4u.core.entitybeans;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Entity()
@Access( AccessType.FIELD )
@EntityListeners( AuditingEntityListener.class )
public class Photo {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    @ManyToOne
    @JoinColumn( name = "profile_fk" )
    @JsonBackReference
    private Profile profile;
    private String name;

    @Column( name = "is_profile_photo" )
    private boolean isProfilePhoto;
    @CreatedDate
    private LocalDateTime created;
    protected Photo(){
    }


    public Photo(String name) {
        this.name = name;
        this.created = LocalDateTime.now();

    }


    // Setter/Getter

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isProfilePhoto() {
        return isProfilePhoto;
    }

    public void setProfilePhoto(boolean profilePhoto) {
        isProfilePhoto = profilePhoto;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override public String toString() {
        return "Photo[" + id + " "+name+"]";
    }
}