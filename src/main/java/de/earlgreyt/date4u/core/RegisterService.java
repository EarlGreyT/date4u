package de.earlgreyt.date4u.core;

import de.earlgreyt.date4u.core.entitybeans.Profile;
import de.earlgreyt.date4u.core.entitybeans.Unicorn;
import de.earlgreyt.date4u.repositories.ProfileRepository;
import de.earlgreyt.date4u.repositories.UnicornRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterService {
    private final UnicornRepository unicornRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public RegisterService(UnicornRepository unicornRepository, ProfileRepository profileRepository, PasswordEncoder passwordEncoder) {
        this.unicornRepository = unicornRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public void register(String email ,String nickName, String password){
        Unicorn unicorn = new Unicorn();
        unicorn.setEmail(email);
        unicorn.setPassword(passwordEncoder.encode(password));
        Profile profile = new Profile(nickName, LocalDate.now(),0,0,null,"", LocalDateTime.now());
        profile.setUnicorn(unicorn);
        profileRepository.save(profile);
    }
}

@Configuration
class RegisterConfig{
    @Bean
    PasswordEncoder encoder(){
        Map<String,PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        return new DelegatingPasswordEncoder("bcrypt",encoders);
    }
}
