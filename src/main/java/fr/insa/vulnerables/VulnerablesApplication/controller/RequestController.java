package fr.insa.vulnerables.VulnerablesApplication.controller;

import fr.insa.vulnerables.VulnerablesApplication.domain.Request;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterRequest;
import fr.insa.vulnerables.VulnerablesApplication.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("add")
    public void addRequest(@RequestBody RegisterRequest registerRequest) {
        requestService.addRequest(registerRequest);
    }
}
