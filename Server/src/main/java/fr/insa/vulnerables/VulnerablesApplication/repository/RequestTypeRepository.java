package fr.insa.vulnerables.VulnerablesApplication.repository;

import fr.insa.vulnerables.VulnerablesApplication.domain.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestTypeRepository extends JpaRepository<RequestType, Long> {

    Integer countByRequestTypeId(Long id);

    RequestType findByRequestTypeId(Long id);
}
