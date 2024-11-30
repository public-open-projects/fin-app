package com.socrates.fin_app.chat.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OpenAIConfigTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public String apiKey() {
            return "test-api-key";
        }

        @Bean
        @Primary
        public String assistantId() {
            return "test-assistant-id";
        }
    }

    @Test
    void openAiRestTemplate_ShouldCreateRestTemplateWithAuthInterceptor() {
        // Given
        OpenAIConfig config = new OpenAIConfig();
        ReflectionTestUtils.setField(config, "apiKey", "test-api-key");

        // When
        RestTemplate restTemplate = config.openAiRestTemplate();

        // Then
        assertNotNull(restTemplate);
        assertEquals(1, restTemplate.getInterceptors().size());
    }

    @Test
    void restTemplateInterceptor_ShouldAddAuthorizationHeader() throws Exception {
        // Given
        OpenAIConfig config = new OpenAIConfig();
        ReflectionTestUtils.setField(config, "apiKey", "test-api-key");
        RestTemplate restTemplate = config.openAiRestTemplate();
        
        HttpRequest request = mock(HttpRequest.class);
        HttpHeaders headers = new HttpHeaders();
        when(request.getHeaders()).thenReturn(headers);
        byte[] body = new byte[0];
        ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);

        // When
        restTemplate.getInterceptors().get(0).intercept(request, body, execution);

        // Then
        verify(request).getHeaders();
        assertTrue(headers.containsKey("Authorization"));
        assertEquals("Bearer test-api-key", headers.getFirst("Authorization"));
        verify(execution).execute(request, body);
    }

    @Test
    void validateAssistantId_ShouldNotThrowException() {
        // Given
        OpenAIConfig config = new OpenAIConfig();
        ReflectionTestUtils.setField(config, "assistantId", "test-assistant-id");

        // When/Then
        assertDoesNotThrow(() -> config.validateAssistantId());
    }

    @Test
    void constructor_ShouldInitializeCorrectly() {
        // When
        OpenAIConfig config = new OpenAIConfig();
        
        // Then
        assertNotNull(config);
    }
}
