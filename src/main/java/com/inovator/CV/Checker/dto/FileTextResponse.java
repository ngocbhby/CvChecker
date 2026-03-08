package com.inovator.CV.Checker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileTextResponse {
    private String fileId;
    private String text;
}
