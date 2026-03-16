package com.inovator.CV.Checker.controller;

import com.inovator.CV.Checker.dto.FileTextResponse;
import com.inovator.CV.Checker.dto.FileUploadResponse;
import com.inovator.CV.Checker.entity.FileDocument;
import com.inovator.CV.Checker.service.FileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/files")
public class FileController {
//ba
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        FileUploadResponse response = fileService.processFile(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        File file = fileService.getFileAsResource(fileId);
        FileDocument doc = fileService.getFileDocument(fileId);
        
        Resource resource = new FileSystemResource(file);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getOriginalFileName() + "\"")
                .contentType(MediaType.parseMediaType(doc.getFileType() != null && !doc.getFileType().isEmpty() 
                        ? doc.getFileType() 
                        : "application/octet-stream"))
                .body(resource);
    }

    @GetMapping("/{fileId}/text")
    public ResponseEntity<FileTextResponse> getFileText(@PathVariable String fileId) {
        FileTextResponse response = fileService.getFileText(fileId);
        return ResponseEntity.ok(response);
    }
}
