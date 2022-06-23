package de.earlgreyt.date4u.repositories;

import de.earlgreyt.date4u.core.entitybeans.Unicorn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnicornRepository extends JpaRepository<Unicorn, Long> {
    boolean existsByEmail(String email);
    Optional<Unicorn> findByEmail(String email);
}
