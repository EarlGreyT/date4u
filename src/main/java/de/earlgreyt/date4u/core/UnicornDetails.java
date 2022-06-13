package de.earlgreyt.date4u.core;

import de.earlgreyt.date4u.core.entitybeans.Profile;
import de.earlgreyt.date4u.core.entitybeans.Unicorn;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Optional;

public class UnicornDetails implements UserDetails {
    private String username;
    private Optional<Profile> profile;
    private String password;

    public UnicornDetails(Unicorn unicorn) {
        this.username = unicorn.getEmail();
        this.password = unicorn.getPassword();
        this.profile = Optional.ofNullable(unicorn.getProfile());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Optional<Profile> getProfile() {
        return profile;
    }
}
