package fr.insa.vulnerables.VulnerablesApplication.repository;

import fr.insa.vulnerables.VulnerablesApplication.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(value = "SELECT * FROM request r WHERE r.request_type_id = :i", nativeQuery = true)
    List<Optional<Request>> findAllByRequestType(@Param("i") Long id);

    @Query(value = "SELECT * FROM request r WHERE r.user_id = :i", nativeQuery = true)
    List<Request> findAllByUserId(@Param("i") Long userId);
}