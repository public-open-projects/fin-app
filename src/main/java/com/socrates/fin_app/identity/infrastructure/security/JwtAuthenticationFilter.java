package com.socrates.fin_app.identity.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.function.Function;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        logger.debug("Processing request: {} {}", request.getMethod(), request.getRequestURI());
        
        try {
            String jwt = getJwtFromRequest(request);
            logger.debug("Extracted JWT token: {}", jwt != null ? "present" : "not present");

            if (StringUtils.hasText(jwt)) {
                DecodedJWT decodedJWT = JWT.decode(jwt);
                String username = decodedJWT.getSubject();
                String role = decodedJWT.getClaim("role").asString();
                logger.debug("Token validation successful. Username: {}, Role: {}", username, role);

                String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                        username, 
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority(authority))
                    );
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Authentication set in SecurityContext");
            }
        } catch (JWTVerificationException ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
        logger.debug("Request processing completed");
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization header: {}", bearerToken);
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            logger.debug("Extracted token from Authorization header");
            return token;
        }
        logger.debug("No valid Bearer token found in Authorization header");
        return null;
    }
}
