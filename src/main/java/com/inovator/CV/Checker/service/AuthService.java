package com.inovator.CV.Checker.service;

import com.inovator.CV.Checker.dto.AuthDtos;
import com.inovator.CV.Checker.entity.User;
import com.inovator.CV.Checker.repository.UserRepository;
import com.inovator.CV.Checker.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.email().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName());
        user.setProvider("local");

        User saved = userRepository.save(user);
        String token = jwtService.generateAccessToken(saved);

        return new AuthDtos.AuthResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getFullName(),
                token
        );
    }

    @Transactional(readOnly = true)
    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        User user = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtService.generateAccessToken(user);

        return new AuthDtos.AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                token
        );
    }
}

