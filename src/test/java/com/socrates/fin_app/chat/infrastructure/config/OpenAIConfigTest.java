package com.socrates.fin_app.chat.infrastructure.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@ActiveProfiles("dev") // Use non-test profile to test actual OpenAI configuration
class OpenAIConfigTest {

    @Autowired
    private OpenAIConfig openAIConfig;

    @Test
    void openAiRestTemplate_ShouldCreateRestTemplateWithAuthInterceptor() {
        // When
        RestTemplate restTemplate = openAIConfig.openAiRestTemplate();

        // Then
        assertNotNull(restTemplate);
        assertEquals(1, restTemplate.getInterceptors().size());
    }

    @Test
    void restTemplateInterceptor_ShouldAddAuthorizationHeader() throws Exception {
        // Given
        RestTemplate restTemplate = openAIConfig.openAiRestTemplate();
        HttpRequest request = mock(HttpRequest.class);
        HttpHeaders headers = new HttpHeaders();
        when(request.getHeaders()).thenReturn(headers);
        byte[] body = new byte[0];
        ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);
        String apiKey = "test-api-key";
        ReflectionTestUtils.setField(openAIConfig, "apiKey", apiKey);

        // When
        restTemplate.getInterceptors().get(0).intercept(request, body, execution);

        // Then
        verify(request).getHeaders();
        assertTrue(headers.containsKey("Authorization"));
        assertEquals("Bearer " + apiKey, headers.getFirst("Authorization"));
        verify(execution).execute(request, body);
    }

    @Test
    void validateAssistantId_ShouldNotThrowException() {
        // When/Then
        assertDoesNotThrow(() -> openAIConfig.validateAssistantId());
    }

    @Test
    void constructor_ShouldInitializeCorrectly() {
        // When
        OpenAIConfig config = new OpenAIConfig();
        
        // Then
        assertNotNull(config);
    }
}
