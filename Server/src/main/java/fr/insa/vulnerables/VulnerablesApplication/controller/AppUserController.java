package fr.insa.vulnerables.VulnerablesApplication.controller;

import fr.insa.vulnerables.VulnerablesApplication.domain.AppUser;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterUser;
import fr.insa.vulnerables.VulnerablesApplication.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    /*
    This method takes in a DTO (RegisterUser) because the user application does not need to provide all the information
    which will be stored on each user, primarily the ID of the user which this application will generate
     */
    @PostMapping("register")
    public void addAppUser(@RequestBody RegisterUser registerUser) {
        appUserService.addAppUser(registerUser);
    }

    @GetMapping("")
    public List<AppUser> getAllUsers() {
        return appUserService.getAllUsers();
    }

    /*
    This method is used in the login process to fetch all the user
    information once the user has already validated themselves
     */
    @GetMapping("username/{username}")
    public AppUser getAppUserByUsername(@PathVariable(required = true) String username) {
        return appUserService.getUserByUsername(username);
    }

    /*
    This method is used by administrators to fetch all the PENDING users to APPROVE/DENY them
     */
    @PreAuthorize("hasRole('Admin')")
    @GetMapping("pending")
    public List<AppUser> getPendingUsers() {
        return appUserService.getPendingUsers();
    }

    /*
    After a user creates an account, administrator needs to approve them before they can make any request or offer
     */
    @PreAuthorize("hasRole('Admin')")
    @PostMapping("change-user-status/{userId}/{newStatus}")
    public void approveUserById(@PathVariable Long userId, @PathVariable String newStatus) {
        appUserService.changeUserStatusByUserId(userId, newStatus);
    }
}
