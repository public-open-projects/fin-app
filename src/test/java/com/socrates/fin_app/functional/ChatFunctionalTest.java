package com.socrates.fin_app.functional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.DefaultResponseErrorHandler;

import com.socrates.fin_app.chat.application.dto.request.InitializeChatRequest;
import com.socrates.fin_app.chat.application.dto.request.CreateMessageRequest;
import com.socrates.fin_app.chat.application.dto.response.InitializeChatResponse;
import com.socrates.fin_app.chat.application.dto.response.CreateMessageResponse;
import com.socrates.fin_app.chat.application.dto.response.RunStatusResponse;
import com.socrates.fin_app.chat.domain.repositories.ChatSessionRepository;
import com.socrates.fin_app.chat.domain.repositories.ChatMessageRepository;

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
class ChatFunctionalTest {
    private static final Logger logger = LoggerFactory.getLogger(ChatFunctionalTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private HttpHeaders headers;
    private HttpHeaders authHeaders;

    @BeforeEach
    void setUp() {
        // Clear repositories
        chatMessageRepository.deleteAll();
        chatSessionRepository.deleteAll();
        
        // Configure basic headers
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Configure authenticated headers with test token
        authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_JSON);
        authHeaders.setBearerAuth("test-auth0-token"); // Match token from TestAuthenticationFilter
        
        // Configure RestTemplate with proper error handling
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getRestTemplate().setErrorHandler(new DefaultResponseErrorHandler() {
            protected boolean hasError(HttpStatus statusCode) {
                return statusCode.is5xxServerError();
            }
        });
    }

    @Test
    @Transactional
    void testCompleteChatFlow() {
        logger.debug("Starting complete chat flow test");
        
        // 1. Initialize chat session
        InitializeChatRequest initRequest = new InitializeChatRequest(
            "test-session",
            false  // unauthenticated flow
        );

        logger.debug("Sending chat initialization request: {}", initRequest);
        ResponseEntity<InitializeChatResponse> initResponse = restTemplate.exchange(
            "/api/chat/initialize",
            HttpMethod.POST,
            new HttpEntity<>(initRequest, headers),
            InitializeChatResponse.class
        );
        
        logger.debug("Chat initialization response: {}", initResponse);
        assertEquals(HttpStatus.OK, initResponse.getStatusCode());
        assertNotNull(initResponse.getBody());
        String threadId = initResponse.getBody().sessionId();
        assertNotNull(threadId);

        // 2. Send a message
        CreateMessageRequest messageRequest = new CreateMessageRequest(
            "Hello, I need help with financing"
        );

        ResponseEntity<CreateMessageResponse> messageResponse = restTemplate.exchange(
            "/api/chat/threads/" + threadId + "/messages",
            HttpMethod.POST,
            new HttpEntity<>(messageRequest, headers),
            CreateMessageResponse.class
        );

        assertEquals(HttpStatus.OK, messageResponse.getStatusCode());
        assertNotNull(messageResponse.getBody());
        String runId = messageResponse.getBody().runId();
        assertNotNull(runId);

        // 3. Check run status
        ResponseEntity<RunStatusResponse> statusResponse = restTemplate.exchange(
            "/api/chat/threads/" + threadId + "/runs/" + runId,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            RunStatusResponse.class
        );

        assertEquals(HttpStatus.OK, statusResponse.getStatusCode());
        assertNotNull(statusResponse.getBody());
        assertNotNull(statusResponse.getBody().status());
        
        // Verify database state
        assertTrue(chatSessionRepository.findById(threadId).isPresent());
        assertFalse(chatMessageRepository.findBySessionIdOrderByTimestampAsc(threadId).isEmpty());
    }

    @Test
    void testValidationScenarios() {
        // Test initialization with invalid session identifier
        InitializeChatRequest invalidRequest = new InitializeChatRequest(
            "",  // empty identifier
            false
        );

        ResponseEntity<String> response = restTemplate.exchange(
            "/api/chat/initialize",
            HttpMethod.POST,
            new HttpEntity<>(invalidRequest, headers),
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Session identifier is required"));

        // Test message creation with empty content
        CreateMessageRequest invalidMessage = new CreateMessageRequest("");

        response = restTemplate.exchange(
            "/api/chat/threads/some-thread-id/messages",
            HttpMethod.POST,
            new HttpEntity<>(invalidMessage, headers),
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Message content is required"));
    }

    @Test
    void testAuthenticatedChatFlow() {
        // Initialize chat with authenticated flag
        InitializeChatRequest authRequest = new InitializeChatRequest(
            "auth-session",
            true
        );

        // Add Bearer token to headers

        ResponseEntity<InitializeChatResponse> response = restTemplate.exchange(
            "/api/chat/initialize",
            HttpMethod.POST,
            new HttpEntity<>(authRequest, authHeaders),
            InitializeChatResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        // Verify that the chat session is marked as authenticated
        String sessionId = response.getBody().sessionId();
        var session = chatSessionRepository.findById(sessionId).orElseThrow();
        assertTrue(session.isAuthenticated());
    }
}
