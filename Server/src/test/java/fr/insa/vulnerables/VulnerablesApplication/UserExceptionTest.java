package fr.insa.vulnerables.VulnerablesApplication;

import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterUser;
import fr.insa.vulnerables.VulnerablesApplication.service.AppUserService;
import fr.insa.vulnerables.VulnerablesApplication.service.RequestDeniedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserExceptionTest {

    private final String username = "user";

    private final RegisterUser registerUser = new RegisterUser(username, "Name", "LastName", "password", 1L);

    @Autowired
    private AppUserService appUserService;

    @Test
    public void testRegisterUser() {
        appUserService.addAppUser(registerUser);
        Long id = appUserService.getIdByUsername(username);
        RequestDeniedException requestDeniedException = assertThrows(RequestDeniedException.class, () -> appUserService.addAppUser(registerUser));
        assertEquals("AppUser with username " + username + " already exists", requestDeniedException.getMessage());
        appUserService.deleteUserById(id);
    }
}
