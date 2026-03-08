package com.inovator.CV.Checker.service.extraction;

import java.io.File;

public interface DocumentExtractionService {
    String extractText(File file);
    boolean supports(String fileType);
}
