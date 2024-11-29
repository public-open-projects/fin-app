package com.socrates.fin_app.functional;

import com.socrates.fin_app.functional.config.TestSecurityConfig;
import com.socrates.fin_app.identity.application.dto.request.*;
import com.socrates.fin_app.identity.application.dto.response.*;
import com.socrates.fin_app.identity.domain.entities.*;
import com.socrates.fin_app.identity.domain.repositories.*;
import org.springframework.web.client.HttpClientErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.main.allow-bean-definition-overriding=true",
        "spring.security.user.name=test",
        "spring.security.user.password=test",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "logging.level.org.springframework.security=DEBUG",
        "logging.level.com.socrates.fin_app=DEBUG"
    }
)
@ActiveProfiles("test")
@Import({TestSecurityConfig.class})
class IdentityFunctionalTest {
    private static final Logger logger = LoggerFactory.getLogger(IdentityFunctionalTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BankerRepository bankerRepository;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        // Clear repositories first
        clientRepository.deleteAll();
        adminRepository.deleteAll();
        bankerRepository.deleteAll();
        
        // Configure headers with basic auth
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // Configure RestTemplate with proper error handling
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getRestTemplate().setErrorHandler(new DefaultResponseErrorHandler() {
            protected boolean hasError(HttpStatus statusCode) {
                // Only treat 5xx errors as errors, let the test handle 4xx
                return statusCode.is5xxServerError();
            }
        });
    }

    @Test
    @Transactional
    void testCompleteClientFlow() {
        logger.debug("Starting complete client flow test");
        
        // 1. Register new client
        ClientRegistrationRequest registrationRequest = new ClientRegistrationRequest(
            "test@example.com",
            "Password123!",
            "John",
            "Doe"
        );

        logger.debug("Sending registration request: {}", registrationRequest);
        ResponseEntity<RegistrationResponse> registrationResponse = restTemplate.exchange(
            "/api/clients/register",
            HttpMethod.POST,
            new HttpEntity<>(registrationRequest, headers),
            RegistrationResponse.class
        );
        logger.debug("Registration response: {}", registrationResponse);

        assertEquals(HttpStatus.OK, registrationResponse.getStatusCode());
        assertNotNull(registrationResponse.getBody());
        String clientId = registrationResponse.getBody().id();

        // 2. Login with new account
        LoginRequest loginRequest = new LoginRequest(
            "test@example.com",
            "Password123!"
        );

        ResponseEntity<AuthenticationResponse> loginResponse = restTemplate.exchange(
            "/api/clients/login",
            HttpMethod.POST,
            new HttpEntity<>(loginRequest, headers),
            AuthenticationResponse.class
        );

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        String token = loginResponse.getBody().token();

        // Create authenticated headers with Bearer token
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_JSON);
        authHeaders.set("Authorization", "Bearer " + token);

        // 3. Update profile with JWT token
        UpdateProfileRequest updateRequest = new UpdateProfileRequest(
            "test@example.com",
            "John",
            "Doe",
            "1234567890"
        );

        String updateUrl = String.format("/api/clients/%s/profile", clientId);
        
        // Add small delay to ensure token processing
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        logger.debug("About to send update profile request. URL: {}, Headers: {}, Request Body: {}", 
            updateUrl, 
            authHeaders, 
            updateRequest);

        ResponseEntity<ProfileResponse> updateResponse = restTemplate.exchange(
            updateUrl,
            HttpMethod.PUT,
            new HttpEntity<>(updateRequest, authHeaders),
            ProfileResponse.class
        );
        
        logger.debug("Received update profile response: {}", updateResponse);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals("John", updateResponse.getBody().firstName());

        // 4. Test password recovery
        ForgotPasswordRequest forgotRequest = new ForgotPasswordRequest("test@example.com");

        ResponseEntity<PasswordRecoveryResponse> recoveryResponse = restTemplate.exchange(
            "/api/clients/forgot-password",
            HttpMethod.POST,
            new HttpEntity<>(forgotRequest, headers),
            PasswordRecoveryResponse.class
        );

        assertEquals(HttpStatus.OK, recoveryResponse.getStatusCode());
        assertNotNull(recoveryResponse.getBody());
    }

    @Test
    @Transactional
    void testAdminFlow() {
        // Create test admin in repository
        AdminProfile admin = new AdminProfile("admin@example.com", "AdminPass123!");
        adminRepository.save(admin);

        LoginRequest loginRequest = new LoginRequest(
            "admin@example.com",
            "AdminPass123!"
        );

        ResponseEntity<AuthenticationResponse> loginResponse = restTemplate.exchange(
            "/api/admins/login",
            HttpMethod.POST,
            new HttpEntity<>(loginRequest, headers),
            AuthenticationResponse.class
        );

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        assertNotNull(loginResponse.getBody().token());

        // Create authenticated headers
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_JSON);
        authHeaders.setBearerAuth(loginResponse.getBody().token());
    }

    @Test
    @Transactional
    void testBankerFlow() {
        // Create test banker in repository
        BankerProfile banker = new BankerProfile("banker@example.com", "BankerPass123!");
        bankerRepository.save(banker);

        // Test banker login
        LoginRequest loginRequest = new LoginRequest(
            "banker@example.com",
            "BankerPass123!"
        );

        ResponseEntity<AuthenticationResponse> loginResponse = restTemplate.exchange(
            "/api/bankers/login",
            HttpMethod.POST,
            new HttpEntity<>(loginRequest, headers),
            AuthenticationResponse.class
        );

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        assertNotNull(loginResponse.getBody().token());
    }


    @Test
    void testValidationScenarios() {
        // Test registration with invalid email format
        ClientRegistrationRequest invalidEmailRequest = new ClientRegistrationRequest(
            "invalid-email",
            "Password123!",
            "John",
            "Doe"
        );

        try {
            restTemplate.exchange(
                "/api/clients/register",
                HttpMethod.POST,
                new HttpEntity<>(invalidEmailRequest, headers),
                String.class
            );
            fail("Expected exception was not thrown");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertTrue(e.getResponseBodyAsString().contains("Invalid email format"));
        }

        // Test registration with short password
        ClientRegistrationRequest shortPasswordRequest = new ClientRegistrationRequest(
            "test@example.com",
            "short",
            "John",
            "Doe"
        );

        try {
            restTemplate.exchange(
                "/api/clients/register",
                HttpMethod.POST,
                new HttpEntity<>(shortPasswordRequest, headers),
                String.class
            );
            fail("Expected exception was not thrown");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertTrue(e.getResponseBodyAsString().contains("Password must be at least 8 characters"));
        }
    }
}
