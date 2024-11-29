package com.socrates.fin_app.identity.interfaces;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.socrates.fin_app.identity.infrastructure.security.JwtAuthenticationFilter;
import com.socrates.fin_app.identity.infrastructure.security.TokenProvider;
import com.socrates.fin_app.identity.infrastructure.security.JwtTokenProvider;
import org.springframework.security.config.Customizer;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints that don't require authentication
                .requestMatchers(
                    "/api/clients/register",
                    "/api/clients/login",
                    "/api/clients/forgot-password",
                    "/api/admins/login",
                    "/api/bankers/login",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()
                // Allow authenticated requests to client profile endpoints
                .requestMatchers("/api/clients/{clientId}/profile").authenticated()
                // Allow all other requests in test environment
                .anyRequest().permitAll())
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider());
    }

    @Bean
    public TokenProvider tokenProvider() {
        // Use a consistent secret key for testing
        return new JwtTokenProvider(
            "testSecretKeyThatIsLongEnoughForHS256AlgorithmAndTesting",
            3600000L
        );
    }
}
