package fr.insa.vulnerables.VulnerablesApplication.service;

import fr.insa.vulnerables.VulnerablesApplication.domain.AppUser;
import fr.insa.vulnerables.VulnerablesApplication.domain.Role;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterUser;
import fr.insa.vulnerables.VulnerablesApplication.repository.AppUserRepository;
import fr.insa.vulnerables.VulnerablesApplication.repository.RequestRepository;
import fr.insa.vulnerables.VulnerablesApplication.repository.RoleRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

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
        appUser.setRating(0);
        appUser.setUserStatus("PENDING");
        appUserRepository.save(appUser);
    }

    @Override
    public AppUser getUserById(Long id) {
        return appUserRepository.findByUserId(id);
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
    public void deleteUserById(Long userId) {
        appUserRepository.deleteById(userId);
    }

    @Override
    public void changeUserStatusByUserId(Long userId, String newStatus) {
        AppUser appUser = appUserRepository.findByUserId(userId);
        appUser.setUserStatus(newStatus);
        appUserRepository.save(appUser);
    }

    @Override
    public AppUser getUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> getPendingUsers() {
        List<AppUser> users = appUserRepository.findAll();
        return users
                .stream()
                .filter(user -> user.getUserStatus().equals("PENDING"))
                .toList();
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("User with username " + username + " not found");
        }

        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(appUser.getRole().getRoleName())
                .build();
    }
}
