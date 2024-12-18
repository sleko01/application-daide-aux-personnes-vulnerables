package fr.insa.vulnerables.VulnerablesApplication.service;

import fr.insa.vulnerables.VulnerablesApplication.domain.Request;
import fr.insa.vulnerables.VulnerablesApplication.domain.RequestType;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterRequest;
import fr.insa.vulnerables.VulnerablesApplication.repository.AppUserRepository;
import fr.insa.vulnerables.VulnerablesApplication.repository.RequestRepository;
import fr.insa.vulnerables.VulnerablesApplication.repository.RequestTypeRepository;
import fr.insa.vulnerables.VulnerablesApplication.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
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
    public void addRequest(RegisterRequest registerRequest) {
        validate(registerRequest);

        Request request = new Request();
        request.setName(registerRequest.getName());
        request.setLocation(registerRequest.getLocation());
        request.setMessage(registerRequest.getMessage());
        request.setRequestType(requestTypeRepository.findByRequestTypeId(registerRequest.getRequestTypeId()));
        request.setAppUser(appUserRepository.findByUserId(registerRequest.getAppUserId()));
        request.setRating(0); // TODO: this needs to be done a different way
        request.setStatus(statusRepository.findByStatusName("Pending")); // sets the status to pending when the status is first created
        requestRepository.save(request);
    }

    @Override
    public List<Request> getAllActiveRequests() {
        return requestRepository.findAllByRequestType(1L)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<Request> getAllPendingRequests() {
        // TODO: do this with actual IDs instead of just 1L/2L/3L
        return requestRepository.findAllByRequestType(2L)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public void changeRequestTypeOfRequest(Long requestId, Long requestTypeId) {
        Request request = this.getRequestById(requestId);
        RequestType requestType = requestTypeRepository.findByRequestTypeId(requestTypeId);
        request.setRequestType(requestType);
    }

    @Override
    public Request getRequestById(Long requestId) {
        // this method will throw a NoSuchElementException if there isn't a request by given ID
        return requestRepository.findById(requestId).orElseThrow();
    }

    private void validate(RegisterRequest registerRequest) {
        Assert.notNull(registerRequest, "RegisterRequest object must be given");
        Assert.hasText(registerRequest.getName(), "RegisterRequest name must be given");
        Assert.hasText(registerRequest.getLocation(), "RegisterRequest location must be given");
        Assert.hasText(registerRequest.getMessage(), "RegisterRequest message must be given");
        if (requestTypeRepository.countByRequestTypeId(registerRequest.getRequestTypeId()) == 0) {
            throw new RequestDeniedException("RequestType with id " + registerRequest.getRequestTypeId() + " does not exist");
        }
        if (appUserRepository.countByUserId(registerRequest.getAppUserId()) == 0) {
            throw new RequestDeniedException("AppUser with id " + registerRequest.getAppUserId() + " does not exist");
        }
    }
}
