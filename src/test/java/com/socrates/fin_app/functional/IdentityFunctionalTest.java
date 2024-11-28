package com.socrates.fin_app.functional;

import com.socrates.fin_app.functional.config.TestSecurityConfig;
import com.socrates.fin_app.identity.application.dto.request.*;
import com.socrates.fin_app.identity.application.dto.response.*;
import com.socrates.fin_app.identity.domain.entities.*;
import com.socrates.fin_app.identity.domain.repositories.*;
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

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.main.allow-bean-definition-overriding=true",
        "spring.security.user.name=test",
        "spring.security.user.password=test"
    }
)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class IdentityFunctionalTest {

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
        
        // Configure RestTemplate
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getRestTemplate().setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(HttpStatus status) {
                return status.series() == HttpStatus.Series.SERVER_ERROR;
            }
        });
    }

    @Test
    @Transactional
    void testCompleteClientFlow() {
        // 1. Register new client (public endpoint)
        ClientRegistrationRequest registrationRequest = new ClientRegistrationRequest(
            "test@example.com",
            "Password123!"
        );

        ResponseEntity<RegistrationResponse> registrationResponse = restTemplate.exchange(
            "/api/clients/register",
            HttpMethod.POST,
            new HttpEntity<>(registrationRequest, headers),
            RegistrationResponse.class
        );

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

        // Create authenticated headers
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_JSON);
        authHeaders.setBearerAuth(token);

        // 3. Update profile with JWT token
        UpdateProfileRequest updateRequest = new UpdateProfileRequest(
            "test@example.com",
            "John",
            "Doe",
            "1234567890"
        );

        ResponseEntity<ProfileResponse> updateResponse = restTemplate.exchange(
            "/api/clients/" + clientId + "/profile",
            HttpMethod.PUT,
            new HttpEntity<>(updateRequest, authHeaders),
            ProfileResponse.class
        );

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals("John", updateResponse.getBody().firstName());

        // 4. Test password recovery (public endpoint)
        ForgotPasswordRequest forgotRequest = new ForgotPasswordRequest(
            "test@example.com"
        );

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
    void testErrorScenarios() {
        // Test registration with existing email (public endpoint)
        ClientRegistrationRequest duplicateRequest = new ClientRegistrationRequest(
            "existing@example.com",
            "Password123!"
        );

        ResponseEntity<RegistrationResponse> firstResponse = restTemplate.exchange(
            "/api/clients/register",
            HttpMethod.POST,
            new HttpEntity<>(duplicateRequest, headers),
            RegistrationResponse.class
        );
        assertEquals(HttpStatus.OK, firstResponse.getStatusCode());

        // Second registration should fail
        ResponseEntity<String> duplicateResponse = restTemplate.exchange(
            "/api/clients/register",
            HttpMethod.POST,
            new HttpEntity<>(duplicateRequest, headers),
            String.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, duplicateResponse.getStatusCode());

        // 2. Test login with invalid credentials
        LoginRequest invalidLogin = new LoginRequest(
            "nonexistent@example.com",
            "WrongPassword"
        );

        ResponseEntity<String> invalidLoginResponse = restTemplate.exchange(
            "/api/clients/login",
            HttpMethod.POST,
            new HttpEntity<>(invalidLogin, headers),
            String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, invalidLoginResponse.getStatusCode());

        // 3. Test password recovery for non-existent email
        ForgotPasswordRequest invalidRecovery = new ForgotPasswordRequest(
            "nonexistent@example.com"
        );

        ResponseEntity<String> invalidRecoveryResponse = restTemplate.exchange(
            "/api/clients/forgot-password",
            HttpMethod.POST,
            new HttpEntity<>(invalidRecovery, headers),
            String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, invalidRecoveryResponse.getStatusCode());

        // 4. Test profile update without authentication
        UpdateProfileRequest updateRequest = new UpdateProfileRequest(
            "test@example.com",
            "John",
            "Doe",
            "1234567890"
        );

        ResponseEntity<String> unauthorizedResponse = restTemplate.exchange(
            "/api/clients/123/profile",
            HttpMethod.PUT,
            new HttpEntity<>(updateRequest, headers),
            String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, unauthorizedResponse.getStatusCode());
    }

    @Test
    void testValidationScenarios() {
        // 1. Test registration with invalid email format
        ClientRegistrationRequest invalidEmailRequest = new ClientRegistrationRequest(
            "invalid-email",
            "Password123!"
        );

        ResponseEntity<String> invalidEmailResponse = restTemplate.exchange(
            "/api/clients/register",
            HttpMethod.POST,
            new HttpEntity<>(invalidEmailRequest, headers),
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, invalidEmailResponse.getStatusCode());

        // 2. Test registration with short password
        ClientRegistrationRequest shortPasswordRequest = new ClientRegistrationRequest(
            "test@example.com",
            "short"
        );

        ResponseEntity<String> shortPasswordResponse = restTemplate.exchange(
            "/api/clients/register",
            HttpMethod.POST,
            new HttpEntity<>(shortPasswordRequest, headers),
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, shortPasswordResponse.getStatusCode());
    }
}
