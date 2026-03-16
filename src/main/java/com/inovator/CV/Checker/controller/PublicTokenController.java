package com.inovator.CV.Checker.controller;

import com.inovator.CV.Checker.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class PublicTokenController {

    private final JwtService jwtService;

    public PublicTokenController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public record UploadTokenResponse(String uploadToken) {}

    @PostMapping("/upload-token")
    public ResponseEntity<UploadTokenResponse> issueUploadToken() {
        return ResponseEntity.ok(new UploadTokenResponse(jwtService.generateUploadToken()));
    }
}

