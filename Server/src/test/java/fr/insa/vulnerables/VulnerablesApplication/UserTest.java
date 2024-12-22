package fr.insa.vulnerables.VulnerablesApplication;

import fr.insa.vulnerables.VulnerablesApplication.domain.AppUser;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterUser;
import fr.insa.vulnerables.VulnerablesApplication.service.AppUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTest {

    private final String username = "user";

    private final RegisterUser registerUser = new RegisterUser(username, "Name", "LastName", "password", 1L);

    @Autowired
    private AppUserService appUserService;

    @Test
    public void testRegisterUser() {
        appUserService.addAppUser(registerUser);
        Long id = appUserService.getIdByUsername(username);
        AppUser testUser = appUserService.getUserById(id);
        assertEquals(username, testUser.getUsername());
        appUserService.deleteUserById(id);
    }
}
