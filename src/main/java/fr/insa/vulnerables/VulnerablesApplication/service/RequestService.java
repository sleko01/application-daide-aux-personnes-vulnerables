package fr.insa.vulnerables.VulnerablesApplication.service;

import fr.insa.vulnerables.VulnerablesApplication.domain.Request;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterRequest;

import java.util.List;

public interface RequestService {
    List<Request> findAll();
    void addRequest(RegisterRequest request);
    List<Request> getAllActiveRequests();
    List<Request> getAllPendingRequests();
    void changeRequestTypeOfRequest(Long requestId, Long requestTypeId);
    Request getRequestById(Long requestId);
}
