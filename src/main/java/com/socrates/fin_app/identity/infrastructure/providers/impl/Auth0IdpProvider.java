package com.socrates.fin_app.identity.infrastructure.providers.impl;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.CreatedUser;
import com.auth0.net.Request;
import com.socrates.fin_app.identity.domain.exceptions.AuthenticationException;
import com.socrates.fin_app.identity.infrastructure.providers.IdpProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class Auth0IdpProvider implements IdpProvider {
    private final AuthAPI auth0Client;
    private final String connection;

    public Auth0IdpProvider(
            AuthAPI auth0Client,
            @Value("${auth0.connection}") String connection) {
        this.auth0Client = auth0Client;
        this.connection = connection;
    }

    @Override
    public void createClientAccount(String email, String password) {
        try {
            Request<CreatedUser> request = auth0Client.signUp(
                email,
                password,
                connection
            );
            
            CreatedUser user = request.execute().getBody();
            if (user == null || user.getEmail() == null) {
                throw new AuthenticationException("Failed to create user account");
            }
            
        } catch (Auth0Exception e) {
            throw new AuthenticationException("Failed to create user account: " + e.getMessage(), e);
        }
    }
}
