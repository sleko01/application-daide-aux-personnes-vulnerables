package fr.insa.vulnerables.VulnerablesApplication.repository;

import fr.insa.vulnerables.VulnerablesApplication.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Status findByStatusName(String statusName);
}
