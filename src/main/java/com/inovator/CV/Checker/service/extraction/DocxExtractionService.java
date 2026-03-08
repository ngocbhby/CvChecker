package com.inovator.CV.Checker.service.extraction;

import com.inovator.CV.Checker.exception.FileException;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class DocxExtractionService implements DocumentExtractionService {

    @Override
    public String extractText(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        } catch (IOException e) {
            throw new FileException("Failed to extract text from DOCX: " + file.getName(), e);
        }
    }

    @Override
    public boolean supports(String fileType) {
        if (fileType == null) return false;
        String lowerCaseFileType = fileType.toLowerCase();
        return lowerCaseFileType.contains("wordprocessingml.document") || lowerCaseFileType.endsWith(".docx");
    }
}
