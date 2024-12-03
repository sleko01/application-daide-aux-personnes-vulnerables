package fr.insa.vulnerables.VulnerablesApplication.repository;

import fr.insa.vulnerables.VulnerablesApplication.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}
