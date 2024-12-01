package fr.insa.vulnerables.VulnerablesApplication.service;

import fr.insa.vulnerables.VulnerablesApplication.domain.AppUser;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Override
    public void addAppUser(RegisterUser registerUser) {
        // TODO: implement addAppUser method
    }

    @Override
    public List<AppUser> getAllUsers() {
        // TODO: implement getAllUsers method
        return List.of();
    }

    @Override
    public Long getIdByUsername(String username) {
        // TODO: implement getIdByUsername method
        return 0L;
    }

    @Override
    public Map<Integer, List<Object>> getAllNotReviewedRequests(Long id) {
        // TODO: implement getAllNotReviewedRequests method
        return Map.of();
    }
}
