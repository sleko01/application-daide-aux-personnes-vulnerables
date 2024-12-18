package fr.insa.vulnerables.VulnerablesApplication.service;

import fr.insa.vulnerables.VulnerablesApplication.domain.AppUser;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterUser;

import java.util.List;
import java.util.Map;

public interface AppUserService {

    void addAppUser(RegisterUser registerUser);

    AppUser getUserById(Long id);

    List<AppUser> getAllUsers();

    Long getIdByUsername(String username);

    Map<Integer, List<Object>> getAllNotReviewedRequests(Long id);

    void deleteUserById(Long userId);

    void approveUserById(Long userId);

    AppUser getUserByUsername(String username);
}
