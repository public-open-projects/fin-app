package com.socrates.fin_app.chat.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("test")
public class TestOpenAIConfig {
    @Bean
    public RestTemplate openAiRestTemplate() {
        return new RestTemplate();
    }
}
