package fr.insa.vulnerables.VulnerablesApplication.repository;

import fr.insa.vulnerables.VulnerablesApplication.domain.AppUser;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Integer countByUsername(String username);

    AppUser findByUsername(String username);

    AppUser findByUserId(Long id);

    @Query(value = "SELECT * FROM appUser a WHERE a.userId != :i", nativeQuery = true)
    List<Optional<AppUser>> findAllExceptCurrentUser(@Param("i") Long id);

    Integer countByUserId(Long id);
}