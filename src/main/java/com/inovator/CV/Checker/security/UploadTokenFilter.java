package com.inovator.CV.Checker.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UploadTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public UploadTokenFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bảo vệ endpoint upload công khai bằng upload-token (JWT scope=upload)
        if (!path.equals("/api/public/files/upload")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            unauthorized(response, "Missing upload token");
            return;
        }

        String token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token)) {
            unauthorized(response, "Invalid upload token");
            return;
        }

        Claims claims = jwtService.parseClaims(token);
        Object scope = claims.get("scope");
        if (!"upload".equals(scope)) {
            unauthorized(response, "Invalid upload token scope");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unauthorized: " + message + "\"}");
    }
}

