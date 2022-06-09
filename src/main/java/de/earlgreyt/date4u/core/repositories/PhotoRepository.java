package de.earlgreyt.date4u.core.repositories;

import de.earlgreyt.date4u.core.entitybeans.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
