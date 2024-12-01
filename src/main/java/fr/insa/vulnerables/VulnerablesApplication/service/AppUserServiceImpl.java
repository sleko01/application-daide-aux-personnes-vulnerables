package fr.insa.vulnerables.VulnerablesApplication.service;

import fr.insa.vulnerables.VulnerablesApplication.domain.AppUser;
import fr.insa.vulnerables.VulnerablesApplication.domain.Role;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterUser;
import fr.insa.vulnerables.VulnerablesApplication.repository.AppUserRepository;
import fr.insa.vulnerables.VulnerablesApplication.repository.RoleRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void addAppUser(RegisterUser registerUser) {
        validate(registerUser);

        if (appUserRepository.countByUsername(registerUser.getUsername()) > 0)
            throw new RequestDeniedException(
                    "AppUser with username " + registerUser.getUsername() + " already exists"
            );

        AppUser appUser = new AppUser();
        String encodedPassword = passwordEncoder.encode(registerUser.getPassword());
        appUser.setFirstName(registerUser.getFirstName());
        appUser.setLastName(registerUser.getLastName());
        appUser.setUsername(registerUser.getUsername());
        appUser.setPassword(encodedPassword);
        Role role = roleRepository.findByRoleId(registerUser.getRoleId());
        appUser.setRole(role);
        appUserRepository.save(appUser);
    }

    @Override
    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public Long getIdByUsername(String username) {
        return appUserRepository.findByUsername(username).getUserId();
    }

    @Override
    public Map<Integer, List<Object>> getAllNotReviewedRequests(Long id) {
        AppUser appUser = appUserRepository.findByUserId(id);
        if (appUser.getRole().getRoleId() != 4) {
            throw new RequestDeniedException(
                    "User with role " + appUser.getRole().getRoleName() + " does not have access"
            );
        }

        Map<Integer, List<Object>> allNotReviewedRequests = new HashMap<>();

        // TODO: two lists, one containing all not reviewed requests and one containing all not reviewed offers
        // List<RequestDog> notReviewedRequestDogs = requestDogRepository.findAllNotReviewed();
        // List<RequestGuardian> notReviewedRequestGuardians = requestGuardianRepository.findAllNotReviewed();

        // allNotReviewedRequests.put(1, Collections.singletonList(notReviewedRequestDogs));
        // allNotReviewedRequests.put(2, Collections.singletonList(notReviewedRequestGuardians));

        return allNotReviewedRequests;
    }

    private void validate(RegisterUser registerUser) {
        Assert.notNull(registerUser, "RegisterUser object must be given");
        Assert.hasText(registerUser.getUsername(), "RegisterUser username must be given");
        Assert.hasText(registerUser.getFirstName(), "RegisterUser first name must be given");
        Assert.hasText(registerUser.getLastName(), "RegisterUser last name must be given");
        if (roleRepository.countByRoleId(registerUser.getRoleId()) == 0)
            throw new RequestDeniedException(
                    "Role with id " + registerUser.getRoleId() + " does not exist"
            );
    }
}
