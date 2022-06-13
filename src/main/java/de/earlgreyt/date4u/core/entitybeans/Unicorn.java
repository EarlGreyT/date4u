package de.earlgreyt.date4u.core.entitybeans;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity()
@Access( AccessType.FIELD )
public class Unicorn {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(name = "email", unique = true)
    private String email;

    private String password;
    @OneToOne
    @JoinColumn( name = "profile_fk" )
    @JsonBackReference
    private Profile profile;
    public Unicorn (String email){
        this.email = email;
    }
    public Unicorn() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }
    // Setter/Getter
}