package fr.insa.vulnerables.VulnerablesApplication;

import fr.insa.vulnerables.VulnerablesApplication.domain.AppUser;
import fr.insa.vulnerables.VulnerablesApplication.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateRequestTest {

    @Autowired
    private AppUserRepository appUserRepository;

    private AppUser admin = appUserRepository.findByUserId(1L); // getting the admin user based on their id

    @Test
    public void testCreateRequest() {

    }
}
