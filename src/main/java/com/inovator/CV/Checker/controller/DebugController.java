package com.inovator.CV.Checker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class DebugController {

    @GetMapping("/whoami")
    public ResponseEntity<String> whoami() {
        return ResponseEntity.ok("cvchecker-backend:new-auth-no-apikey");
    }
}

