package de.earlgreyt.date4u.core;

import de.earlgreyt.date4u.core.entitybeans.Profile;
import de.earlgreyt.date4u.core.entitybeans.Unicorn;
import de.earlgreyt.date4u.core.exceptions.EmailAlreadyInUseException;
import de.earlgreyt.date4u.core.formdata.UserDTO;
import de.earlgreyt.date4u.repositories.ProfileRepository;
import de.earlgreyt.date4u.repositories.UnicornRepository;
import javax.transaction.Transactional;
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
    @Transactional
    public void register(UserDTO userDTO)throws EmailAlreadyInUseException {
        if (unicornRepository.findByEmail(userDTO.getEmail()).isPresent()){
            throw new EmailAlreadyInUseException();
        }
        Unicorn unicorn = new Unicorn();
        unicorn.setEmail(userDTO.getEmail());
        unicorn.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Profile profile = new Profile(userDTO.getNickname(), LocalDate.now(),0,0,null,"", LocalDateTime.now());
        profileRepository.save(profile);
        unicorn.setProfile(profile);
        unicornRepository.save(unicorn);
    }
}

