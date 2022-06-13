package de.earlgreyt.date4u.core;

import de.earlgreyt.date4u.core.entitybeans.Unicorn;
import de.earlgreyt.date4u.core.repositories.UnicornRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UnicornDetailService implements UserDetailsService {
    private final UnicornRepository unicornRepository;
    @Autowired
    public UnicornDetailService(UnicornRepository unicornRepository) {
        this.unicornRepository = unicornRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Unicorn> unicorn = unicornRepository.findByEmail(username);
        if (unicorn.isEmpty()) {
           throw new UsernameNotFoundException("cant find a user with the given name");
        }
        return new UnicornDetails(unicorn.get());
    }
}
