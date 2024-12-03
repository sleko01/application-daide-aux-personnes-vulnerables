package fr.insa.vulnerables.VulnerablesApplication.controller;

import fr.insa.vulnerables.VulnerablesApplication.domain.AppUser;
import fr.insa.vulnerables.VulnerablesApplication.domain.Role;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterUser;
import fr.insa.vulnerables.VulnerablesApplication.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("users")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/register")
    public void addAppUser(@RequestBody RegisterUser registerUser) {
        appUserService.addAppUser(registerUser);
    }

    @GetMapping("")
    public List<AppUser> getAllUsers() {
        return appUserService.getAllUsers();
    }

    @GetMapping("username/{username}")
    public Long getAppUserIdByUsername(@PathVariable(required = true) String username) {
        return appUserService.getIdByUsername(username);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("moderation/{id}/r")
    public Map<Integer, List<Object>> getAllNotReviewedRequests(@PathVariable(required = true) Long id) {
        return appUserService.getAllNotReviewedRequests(id);
    }
}
