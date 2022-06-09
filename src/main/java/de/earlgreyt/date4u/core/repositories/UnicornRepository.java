package de.earlgreyt.date4u.core.repositories;

import com.tutego.date4u.core.entitybeans.Unicorn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnicornRepository extends JpaRepository<Unicorn, Long> {
    boolean existsByEmail(String email);
}
