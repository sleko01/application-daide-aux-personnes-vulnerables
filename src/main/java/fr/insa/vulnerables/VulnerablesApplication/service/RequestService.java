package fr.insa.vulnerables.VulnerablesApplication.service;

import fr.insa.vulnerables.VulnerablesApplication.domain.Request;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterRequest;

import java.util.List;

public interface RequestService {
    List<Request> findAll();
    Request addRequest(RegisterRequest request);
    void changeRequestStatus(Long requestId, String statusName);
    Request getRequestById(Long requestId);
    List<Request> getRequestsByUserId(Long userId);
    List<Request> getAllActiveRequestsApartFromCurrentUser(Long userId);
    List<Request> getAllPendingRequestsAndOffers();
    void rateRequest(Long requestId, int rating);
    List<Request> getAllAcceptedInProgressRequests(Long userId);
    void acceptRequest(Long requestId, Long userId);
    void deleteRequestById(Long requestId);
}
