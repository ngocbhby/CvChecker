package com.inovator.CV.Checker.controller;

import com.inovator.CV.Checker.dto.FileUploadResponse;
import com.inovator.CV.Checker.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Các endpoint upload công khai, bảo vệ bằng upload-token ngắn hạn (Authorization: Bearer <uploadToken>).
 */
@RestController
@RequestMapping("/api/public/files")
public class PublicFileController {

    private final FileService fileService;

    public PublicFileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        FileUploadResponse response = fileService.processFile(file);
        return ResponseEntity.ok(response);
    }
}

