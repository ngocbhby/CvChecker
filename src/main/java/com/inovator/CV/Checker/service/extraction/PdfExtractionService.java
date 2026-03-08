package com.inovator.CV.Checker.service.extraction;

import com.inovator.CV.Checker.exception.FileException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PdfExtractionService implements DocumentExtractionService {

    @Override
    public String extractText(File file) {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            throw new FileException("Failed to extract text from PDF: " + file.getName(), e);
        }
    }

    @Override
    public boolean supports(String fileType) {
        if (fileType == null) return false;
        String lowerCaseFileType = fileType.toLowerCase();
        return lowerCaseFileType.equals("application/pdf") || lowerCaseFileType.endsWith(".pdf");
    }
}
