package com.socrates.fin_app.identity;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.DefaultResponseErrorHandler;
import com.socrates.fin_app.identity.domain.repositories.AdminRepository;
import com.socrates.fin_app.identity.domain.repositories.BankerRepository;
import com.socrates.fin_app.identity.domain.repositories.ClientRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IdentityFunctionalTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected ClientRepository clientRepository;

    @Autowired
    protected AdminRepository adminRepository;

    @Autowired
    protected BankerRepository bankerRepository;

    protected HttpHeaders headers;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // Clear repositories
        clientRepository.deleteAll();
        adminRepository.deleteAll();
        bankerRepository.deleteAll();
        
        // Configure RestTemplate to not follow redirects
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getRestTemplate().setErrorHandler(new DefaultResponseErrorHandler());
    }
}
