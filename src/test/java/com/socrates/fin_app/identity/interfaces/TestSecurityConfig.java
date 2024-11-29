package com.socrates.fin_app.identity.interfaces;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.socrates.fin_app.identity.infrastructure.security.JwtAuthenticationFilter;
import com.socrates.fin_app.identity.infrastructure.security.TokenProvider;
import com.socrates.fin_app.identity.infrastructure.security.JwtTokenProvider;
import org.springframework.security.config.Customizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(TestSecurityConfig.class);
    private static final String SECRET_KEY = "testSecretKeyThatIsLongEnoughForHS256AlgorithmAndTesting";
    private static final long TOKEN_VALIDITY = 3600000L; // 1 hour

    @Bean
    @Primary
    public TokenProvider tokenProvider() {
        return new JwtTokenProvider(SECRET_KEY, TOKEN_VALIDITY);
    }

    @Bean
    @Primary
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.debug("Configuring security filter chain");
        
        http
            .csrf(csrf -> {
                csrf.disable();
                logger.debug("CSRF disabled");
            })
            .cors(cors -> {
                Customizer.withDefaults().customize(cors);
                logger.debug("CORS configured with defaults");
            })
            .sessionManagement(session -> {
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                logger.debug("Session management configured as STATELESS");
            })
            .authorizeHttpRequests(auth -> {
                logger.debug("Configuring authorization rules");
                auth
                    .requestMatchers(
                        "/api/clients/register",
                        "/api/clients/login",
                        "/api/clients/forgot-password",
                        "/api/admins/login",
                        "/api/bankers/login",
                        "/error",
                        "/api/clients/**",  // Temporarily allow all client endpoints for testing
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                    ).permitAll()
                    .anyRequest().authenticated();
                logger.debug("Authorization rules configured");
            })
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        logger.debug("Security filter chain configuration completed");
        return http.build();
    }
}
