package com.socrates.fin_app.identity.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Auth0TokenProvider implements TokenProvider {

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.clientSecret}")
    private String clientSecret;

    @Override
    public String createToken(String subject, String role) {
        return JWT.create()
                .withSubject(subject)
                .withClaim("https://" + domain + "/roles", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 864000000)) // 10 days
                .sign(Algorithm.HMAC256(clientSecret));
    }

    @Override
    public String getSubjectFromToken(String token) {
        return JWT.require(Algorithm.HMAC256(clientSecret))
                .build()
                .verify(token)
                .getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(clientSecret))
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
