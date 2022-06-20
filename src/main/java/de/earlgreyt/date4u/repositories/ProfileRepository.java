package de.earlgreyt.date4u.repositories;


import de.earlgreyt.date4u.core.entitybeans.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {
  // @Modifying
  // @Query("UPDATE Profile p SET p.photos =: photos WHERE p.id = :id")
  // int updatePhotos(long id, List<Photo> photos);
  @Query("SELECT p FROM Profile p where p.hornlength between :minSize and :maxSize")
  List<Profile> findWithHornsizeBetween(short minSize, short maxSize);

  @Query("SELECT p FROM Profile p WHERE p.nickname = :name")
  Optional<Profile> findProfileByNickname(String name);

  @Query("SELECT p FROM Profile p WHERE p.nickname LIKE %:name%")
  List<Profile> findProfilesByContainingName(String name);
  @Modifying
  @Query("UPDATE Profile p SET p.lastseen =:lastseen WHERE p.id = :id")
  int updateLastSeen(long id, LocalDateTime lastseen);
  @Query("SELECT p.id as id, p.nickname as nickname FROM Profile p WHERE id=:k")
  Map<String, Object> findSimplifiedProfile(long k);

}
