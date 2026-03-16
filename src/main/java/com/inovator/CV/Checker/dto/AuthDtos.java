package com.inovator.CV.Checker.dto;

public class AuthDtos {

    public record RegisterRequest(
            String email,
            String password,
            String fullName
    ) {}

    public record LoginRequest(
            String email,
            String password
    ) {}

    public record AuthResponse(
            Long userId,
            String email,
            String fullName,
            String accessToken
    ) {}
}

