
package com.curescript.security;

import com.curescript.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        // Temporary diagnostics: never print the actual token
        System.out.println("Request path: " + request.getRequestURI());
        System.out.println(
                "Authorization header present: "
                        + (authorizationHeader != null)
        );
        System.out.println(
                "Bearer prefix present: "
                        + (authorizationHeader != null
                        && authorizationHeader.startsWith("Bearer "))
        );

        // No token: let Spring Security handle authentication
        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7).trim();

        if (token.isEmpty()) {
            SecurityContextHolder.clearContext();
            sendUnauthorized(response, "Empty Bearer token");
            return;
        }

        try {
            Claims claims =
                    jwtService.validateAndExtractClaims(token);

            String email = claims.getSubject();

            if (email == null || email.isBlank()) {
                SecurityContextHolder.clearContext();
                sendUnauthorized(response, "Token subject is missing");
                return;
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            Collections.emptyList()
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

            System.out.println("JWT validation successful");

        } catch (JwtException | IllegalArgumentException exception) {
            SecurityContextHolder.clearContext();

            // Log only the exception class, not the token
            System.out.println(
                    "JWT validation failed: "
                            + exception.getClass().getSimpleName()
            );

            sendUnauthorized(response, "Invalid or expired token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendUnauthorized(
            HttpServletResponse response,
            String message) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                "{\"error\":\"Unauthorized\","
                        + "\"message\":\"" + message + "\","
                        + "\"status\":401}"
        );
    }
}
