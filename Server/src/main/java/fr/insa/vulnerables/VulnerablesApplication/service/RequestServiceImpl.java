package fr.insa.vulnerables.VulnerablesApplication.service;

import fr.insa.vulnerables.VulnerablesApplication.domain.AppUser;
import fr.insa.vulnerables.VulnerablesApplication.domain.Request;
import fr.insa.vulnerables.VulnerablesApplication.domain.Status;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterRequest;
import fr.insa.vulnerables.VulnerablesApplication.repository.AppUserRepository;
import fr.insa.vulnerables.VulnerablesApplication.repository.RequestRepository;
import fr.insa.vulnerables.VulnerablesApplication.repository.RequestTypeRepository;
import fr.insa.vulnerables.VulnerablesApplication.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestTypeRepository requestTypeRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public List<Request> findAll() {
        return requestRepository.findAll();
    }

    @Override
    public Request addRequest(RegisterRequest registerRequest) {
        validate(registerRequest);

        Request request = new Request();
        request.setName(registerRequest.getName());
        request.setLocation(registerRequest.getLocation());
        request.setMessage(registerRequest.getMessage());
        request.setRequestType(requestTypeRepository.findByRequestTypeId(registerRequest.getRequestTypeId()));
        request.setAppUser(appUserRepository.findByUserId(registerRequest.getAppUserId()));
        request.setRating(0);
        request.setStatus(statusRepository.findByStatusName("Pending")); // sets the status to pending when the status is first created
        requestRepository.save(request);
        return request;
    }

    @Override
    public void changeRequestStatus(Long requestId, String statusName) {
        Request request = this.getRequestById(requestId);
        Status status = statusRepository.findByStatusName(statusName);
        request.setStatus(status);
        requestRepository.save(request);
    }

    @Override
    public Request getRequestById(Long requestId) {
        // this method will throw a NoSuchElementException if there isn't a request by given ID
        return requestRepository.findById(requestId).orElseThrow();
    }

    @Override
    public List<Request> getRequestsByUserId(Long userId) {
        return requestRepository.findAllByUserId(userId);
    }

    @Override
    public List<Request> getAllActiveRequestsApartFromCurrentUser(Long userId) {
        AppUser appUser = appUserRepository.findByUserId(userId);
        List<Request> allActiveRequestsAndOffers = requestRepository.findAll()
                .stream()
                .filter(request -> request.getStatus().getStatusName().equals("Validated"))
                .filter(request -> !Objects.equals(request.getAppUser().getUserId(), userId))
                .toList();
        if (appUser.getRole().getRoleId() == 1L) {
            // user is a vulnerable person, so return all active offers
            return allActiveRequestsAndOffers.stream()
                    .filter(request -> request.getRequestType().getRequestTypeId() == 2L)
                    .collect(Collectors.toList());
        } else if (appUser.getRole().getRoleId() == 2L) {
            // user is a volunteer, so return all active requests
            return allActiveRequestsAndOffers.stream()
                    .filter(request -> request.getRequestType().getRequestTypeId() == 1L)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("User with ID " + userId + " has an invalid role for this request");
        }
    }

    @Override
    public List<Request> getAllPendingRequestsAndOffers() {
        return requestRepository.findAll()
                .stream()
                .filter(request -> request.getStatus().getStatusName().equals("Pending"))
                .collect(Collectors.toList());
    }

    @Override
    public void rateRequest(Long requestId, int rating) {
        Request request = this.getRequestById(requestId);
        request.setRating(rating);
        AppUser appUser = appUserRepository.findByUserId(request.getAppUser().getUserId());
        if (appUser.getRating() == 0) {
            appUser.setRating(rating);
        } else {
            long numberOfRatings = requestRepository.findAllByUserId(appUser.getUserId())
                    .stream()
                    .filter(r -> r.getRating() != 0)
                    .count();
            appUser.setRating((appUser.getRating() + rating) / numberOfRatings);
        }
        request.setStatus(statusRepository.findByStatusName("Completed"));
        appUserRepository.save(appUser);
        requestRepository.save(request);
    }

    @Override
    public List<Request> getAllAcceptedInProgressRequests(Long userId) {
        return requestRepository.findAll()
                .stream()
                .filter(request -> request.getStatus().getStatusName().equals("InProgress"))
                .filter(request -> Objects.equals(request.getUserAccepted().getUserId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public void acceptRequest(Long requestId, Long userId) {
        Request request = this.getRequestById(requestId);
        AppUser appUser = appUserRepository.findByUserId(userId);
        request.setUserAccepted(appUser);
        request.setStatus(statusRepository.findByStatusName("InProgress"));
        requestRepository.save(request);
    }

    @Override
    public void deleteRequestById(Long requestId) {
        requestRepository.deleteById(requestId);
    }

    private void validate(RegisterRequest registerRequest) {
        Assert.notNull(registerRequest, "RegisterRequest object must be given");
        Assert.hasText(registerRequest.getName(), "RegisterRequest name must be given");
        Assert.hasText(registerRequest.getLocation(), "RegisterRequest location must be given");
        Assert.hasText(registerRequest.getMessage(), "RegisterRequest message must be given");
        Long appUserRoleId = appUserRepository.findByUserId(registerRequest.getAppUserId()).getRole().getRoleId();
        if (requestTypeRepository.countByRequestTypeId(registerRequest.getRequestTypeId()) == 0) {
            throw new RequestDeniedException("RequestType with id " + registerRequest.getRequestTypeId() + " does not exist");
        }
        if (appUserRepository.countByUserId(registerRequest.getAppUserId()) == 0) {
            throw new RequestDeniedException("AppUser with id " + registerRequest.getAppUserId() + " does not exist");
        }
        if (!Objects.equals(registerRequest.getRequestTypeId(), appUserRoleId)) {
            throw new RequestDeniedException("AppUser with id " + registerRequest.getAppUserId() + " has an invalid role for this request");
        }
    }
}
