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

    private static final String SECRET_KEY = "testSecretKeyThatIsLongEnoughForHS256AlgorithmAndTesting";
    private static final long TOKEN_VALIDITY = 3600000L; // 1 hour

    @Bean
    @Primary
    public TokenProvider tokenProvider() {
        return new JwtTokenProvider(SECRET_KEY, TOKEN_VALIDITY);
    }

    @Bean
    @Primary
    public JwtAuthenticationFilter jwtAuthenticationFilter(TokenProvider tokenProvider) {
        return new JwtAuthenticationFilter(tokenProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/clients/register",
                    "/api/clients/login",
                    "/api/clients/forgot-password",
                    "/api/admins/login",
                    "/api/bankers/login",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()
                .requestMatchers("/api/clients/{clientId}/profile").hasAnyRole("CLIENT", "ADMIN")
                .anyRequest().authenticated())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
}
