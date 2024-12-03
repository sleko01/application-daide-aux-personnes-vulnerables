package fr.insa.vulnerables.VulnerablesApplication.controller;

import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterRequest;
import fr.insa.vulnerables.VulnerablesApplication.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @GetMapping("")
    public void getAllRequests() {
        requestService.findAll();
    }

    @PostMapping
    public void addRequest(@RequestBody RegisterRequest registerRequest) {
        requestService.addRequest(registerRequest);
    }
}
