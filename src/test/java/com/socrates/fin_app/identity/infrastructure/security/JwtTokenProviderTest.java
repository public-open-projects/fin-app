package com.socrates.fin_app.identity.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
    }

    @Test
    void whenCreatingToken_thenTokenIsCreatedWithCorrectClaims() {
        // Given
        String username = "test@example.com";
        String role = "CLIENT";

        // When
        String token = jwtTokenProvider.createToken(username, role);

        // Then
        assertNotNull(token);
        assertEquals(username, jwtTokenProvider.getUsername(token));
        assertEquals(role, jwtTokenProvider.getRole(token));
    }

    @Test
    void whenGettingUsername_thenCorrectUsernameIsReturned() {
        // Given
        String expectedUsername = "test@example.com";
        String token = jwtTokenProvider.createToken(expectedUsername, "CLIENT");

        // When
        String actualUsername = jwtTokenProvider.getUsername(token);

        // Then
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    void whenGettingRole_thenCorrectRoleIsReturned() {
        // Given
        String expectedRole = "ADMIN";
        String token = jwtTokenProvider.createToken("admin@example.com", expectedRole);

        // When
        String actualRole = jwtTokenProvider.getRole(token);

        // Then
        assertEquals(expectedRole, actualRole);
    }
}
