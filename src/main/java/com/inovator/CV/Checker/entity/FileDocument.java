package com.inovator.CV.Checker.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FileDocument {
    private String id;
    private String originalFileName;
    private String storedFilePath;
    private String fileType;
    private LocalDateTime uploadedAt;
}
