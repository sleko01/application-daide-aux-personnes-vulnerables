package fr.insa.vulnerables.VulnerablesApplication.service;

import fr.insa.vulnerables.VulnerablesApplication.domain.AppUser;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterUser;

import java.util.List;

public interface AppUserService {

    void addAppUser(RegisterUser registerUser);

    AppUser getUserById(Long id);

    List<AppUser> getAllUsers();

    Long getIdByUsername(String username);

    void deleteUserById(Long userId);

    void changeUserStatusByUserId(Long userId, String newStatus);

    AppUser getUserByUsername(String username);

    List<AppUser> getPendingUsers();
}
