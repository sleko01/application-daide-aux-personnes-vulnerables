package fr.insa.vulnerables.VulnerablesApplication;

import fr.insa.vulnerables.VulnerablesApplication.domain.AppUser;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterRequest;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterUser;
import fr.insa.vulnerables.VulnerablesApplication.service.AppUserService;
import fr.insa.vulnerables.VulnerablesApplication.service.RequestDeniedException;
import fr.insa.vulnerables.VulnerablesApplication.service.RequestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateRequestException {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private RequestService requestService;

    /* setting the wrong role for the user, so it causes an exception because a user with role 2 which corresponds to ???
    should not be able to create a request, they should only be able to create an offer */
    private RegisterUser user = new RegisterUser(
            "testUser", "TestName", "TestLastName", "password", 2L
    );

    @Test
    public void testCreateRequestException() {
        appUserService.addAppUser(user);
        AppUser createdUser = appUserService.getUserByUsername(user.getUsername());
        RegisterRequest registerRequest = new RegisterRequest(1L, createdUser.getUserId(), "Name", "Location", "Message");
        RequestDeniedException requestDeniedException = assertThrows(RequestDeniedException.class, () -> requestService.addRequest(registerRequest));
        try {
            assertEquals("AppUser with id " + registerRequest.getAppUserId() + " has an invalid role for this request", requestDeniedException.getMessage());
        } finally {
            appUserService.deleteUserById(createdUser.getUserId());
        }
    }
}
