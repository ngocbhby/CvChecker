package com.inovator.CV.Checker.service.extraction;

import com.inovator.CV.Checker.exception.UnsupportedFileTypeException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtractionFactory {

    private final List<DocumentExtractionService> extractionServices;

    public ExtractionFactory(List<DocumentExtractionService> extractionServices) {
        this.extractionServices = extractionServices;
    }

    public DocumentExtractionService getExtractor(String fileType) {
        return extractionServices.stream()
                .filter(service -> service.supports(fileType))
                .findFirst()
                .orElseThrow(() -> new UnsupportedFileTypeException("Unsupported file type or extension: " + fileType + ". Only PDF and DOCX are allowed."));
    }
}
