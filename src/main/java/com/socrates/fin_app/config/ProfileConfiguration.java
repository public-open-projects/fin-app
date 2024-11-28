package com.socrates.fin_app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
public class ProfileConfiguration {

    @Bean
    @Profile("dev")
    public String developmentBean() {
        return "Development Environment";
    }

    @Bean
    @Profile("test")
    public String testBean() {
        return "Test Environment";
    }

    @Bean
    @Profile("prod")
    public String productionBean() {
        return "Production Environment";
    }
}
