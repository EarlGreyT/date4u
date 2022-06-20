package de.earlgreyt.date4u.repositories;

import de.earlgreyt.date4u.core.entitybeans.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Optional<Photo> findByName(String name);
}
