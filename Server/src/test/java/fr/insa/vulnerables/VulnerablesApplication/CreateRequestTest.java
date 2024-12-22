package fr.insa.vulnerables.VulnerablesApplication;

import fr.insa.vulnerables.VulnerablesApplication.domain.AppUser;
import fr.insa.vulnerables.VulnerablesApplication.domain.Request;
import fr.insa.vulnerables.VulnerablesApplication.dto.RegisterRequest;
import fr.insa.vulnerables.VulnerablesApplication.repository.AppUserRepository;
import fr.insa.vulnerables.VulnerablesApplication.service.RequestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateRequestTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RequestService requestService;

    @Test
    public void testCreateRequest() {
        AppUser vulnerable = appUserRepository.findByUsername("vulnerable");
        RegisterRequest registerRequest = new RegisterRequest(1L, vulnerable.getUserId(), "Name", "Location", "Message");
        Request request = requestService.addRequest(registerRequest);
        assertEquals(vulnerable.getUserId(), request.getAppUser().getUserId());
        requestService.deleteRequestById(request.getRequestId());
    }
}
