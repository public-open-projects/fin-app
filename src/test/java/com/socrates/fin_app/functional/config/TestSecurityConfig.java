package com.socrates.fin_app.functional.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import com.socrates.fin_app.identity.infrastructure.security.JwtAuthenticationFilter;
import com.socrates.fin_app.identity.infrastructure.security.TokenProvider;
import com.socrates.fin_app.identity.infrastructure.security.JwtTokenProvider;

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
                .requestMatchers("/api/clients/register", 
                               "/api/clients/login",
                               "/api/clients/forgot-password",
                               "/api/admins/login",
                               "/api/bankers/login",
                               "/error").permitAll()
                .anyRequest().authenticated())
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .httpBasic(basic -> basic
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), authException.getMessage());
                }));
            
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider());
    }

    @Bean
    public TokenProvider tokenProvider() {
        return new JwtTokenProvider("testSecretKeyThatIsLongEnoughForHS256Algorithm", 3600000L);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails testUser = User.withDefaultPasswordEncoder()
            .username("test@example.com")  // Match the test email
            .password("Password123!")      // Match the test password
            .roles("CLIENT")              // Add appropriate role
            .build();
        return new InMemoryUserDetailsManager(testUser);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
