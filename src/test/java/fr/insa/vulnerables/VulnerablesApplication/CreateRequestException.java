package fr.insa.vulnerables.VulnerablesApplication;

import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterUser;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateRequestException {

    /* setting the wrong role for the user, so it causes an exception because a user with role 2 which corresponds to ???
    should not be able to create a request, they should only be able to create an offer */
    private RegisterUser user = new RegisterUser(
            "testUser", "TestName", "TestLastName", "password", 2L
    );

}
