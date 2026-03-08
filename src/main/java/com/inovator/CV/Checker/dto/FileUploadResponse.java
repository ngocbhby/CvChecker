package com.inovator.CV.Checker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileUploadResponse {
    private String fileId;
    private String fileName;
    private String filePath;
    private String text;
}
