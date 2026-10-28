
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

        // No token: let Spring Security handle authentication
        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7).trim();

        // Reject an empty Bearer token
        if (token.isEmpty()) {
            sendUnauthorized(response);
            return;
        }

        try {
            Claims claims =
                    jwtService.validateAndExtractClaims(token);

            String email = claims.getSubject();

            if (email == null || email.isBlank()) {
                sendUnauthorized(response);
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

        } catch (JwtException | IllegalArgumentException exception) {
            SecurityContextHolder.clearContext();
            sendUnauthorized(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse response)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                "{\"error\":\"Unauthorized\","
                        + "\"message\":\"Invalid or expired token\","
                        + "\"status\":401}"
        );
    }
}
