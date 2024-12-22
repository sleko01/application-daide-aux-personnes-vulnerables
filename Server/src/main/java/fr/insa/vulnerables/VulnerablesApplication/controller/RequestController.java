package fr.insa.vulnerables.VulnerablesApplication.controller;

import fr.insa.vulnerables.VulnerablesApplication.domain.Request;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterRequest;
import fr.insa.vulnerables.VulnerablesApplication.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @GetMapping("")
    public List<Request> getAllRequests() {
        return requestService.findAll();
    }

    /*
    This method is used by admins to fetch all pending requests and offers to be approved or rejected
     */
    @PreAuthorize("hasRole('Admin')")
    @GetMapping("pending-requests-offers")
    public List<Request> getAllPendingRequestsAndOffers() {
        return requestService.getAllPendingRequestsAndOffers();
    }

    /*
    This method is used by users(both vulnerable and volunteer) to add a request/offer
     */
    @PostMapping("add")
    public void addRequest(@RequestBody RegisterRequest registerRequest) {
        requestService.addRequest(registerRequest);
    }

    /*
    This method is used by admins to change the type of request, usually from PENDING to APPROVED/REJECTED
    It is also used by users to change the status of request/offer to IN_PROGRESS
     */
    @PostMapping("change-request-status/{requestId}/{statusName}")
    public void changeRequestStatus(@PathVariable(required = true) Long requestId, @PathVariable String statusName) {
        requestService.changeRequestStatus(requestId, statusName);
    }

    /*
    This method is used by users to accept a request/offer, userId represents the user who is accepting the request
     */
    @PutMapping("accept/{requestId}/{userId}")
    public void acceptRequest(@PathVariable(required = true) Long requestId, @PathVariable(required = true) Long userId) {
        requestService.acceptRequest(requestId, userId);
    }

    /*
    This method fetches all the requests or offers made by a user
     */
    @GetMapping("user/{userId}")
    public List<Request> getRequestsByUserId(@PathVariable(required = true) Long userId) {
        return requestService.getRequestsByUserId(userId);
    }

    /*
    This method is used by users to fetch all active requests/offers apart from their own, so they can accept request/offer
     */
    @GetMapping("all-active-apart-from-current-user/{userId}")
    public List<Request> getAllActiveRequestsApartFromCurrentUser(@PathVariable(required = true) Long userId) {
        return requestService.getAllActiveRequestsApartFromCurrentUser(userId);
    }

    /*
    This method will fetch all the requests/offers that have been accepted by the user who calls this method
     */
    @GetMapping("all-accepted-in-progress/{userId}")
    public List<Request> getAllAcceptedInProgressRequests(@PathVariable(required = true) Long userId) {
        return requestService.getAllAcceptedInProgressRequests(userId);
    }

    /*
    Called when a user rates another user's request, the calculation is based off of the average of all ratings
     */
    @PutMapping("rate/{requestId}/{rating}")
    public void rateRequest(@PathVariable(required = true) Long requestId, @PathVariable(required = true) int rating) {
        requestService.rateRequest(requestId, rating);
    }
}
