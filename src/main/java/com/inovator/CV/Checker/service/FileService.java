package com.inovator.CV.Checker.service;

import com.inovator.CV.Checker.dto.FileTextResponse;
import com.inovator.CV.Checker.dto.FileUploadResponse;
import com.inovator.CV.Checker.entity.FileDocument;
import com.inovator.CV.Checker.exception.DocumentNotFoundException;
import com.inovator.CV.Checker.exception.FileException;
import com.inovator.CV.Checker.mapper.FileMapper;
import com.inovator.CV.Checker.repository.FileDocumentRepository;
import com.inovator.CV.Checker.service.extraction.DocumentExtractionService;
import com.inovator.CV.Checker.service.extraction.ExtractionFactory;
import com.inovator.CV.Checker.service.storage.LocalFileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileService {

    private final LocalFileStorageService storageService;
    private final ExtractionFactory extractionFactory;
    private final FileDocumentRepository repository;
    private final FileMapper fileMapper;

    public FileService(LocalFileStorageService storageService,
                       ExtractionFactory extractionFactory,
                       FileDocumentRepository repository,
                       FileMapper fileMapper) {
        this.storageService = storageService;
        this.extractionFactory = extractionFactory;
        this.repository = repository;
        this.fileMapper = fileMapper;

    }

    public FileUploadResponse processFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = "";
        if (fileName != null && fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        }
        
        String contentType = file.getContentType() != null && !file.getContentType().equals("application/octet-stream") 
                ? file.getContentType() 
                : extension;

        DocumentExtractionService extractor = extractionFactory.getExtractor(contentType);

        String fileId = UUID.randomUUID().toString();
        
        String storedFilePath = storageService.storeFile(file, fileId);

        String text;
        try {
            File savedFile = storageService.getFilePath(storedFilePath).toFile();
            text = extractor.extractText(savedFile);
        } catch (Exception e) {
            throw new FileException("Failed to extract text from file.", e);
        }

        FileDocument doc = FileDocument.builder()
                .id(fileId)
                .originalFileName(fileName)
                .storedFilePath(storedFilePath)
                .fileType(contentType)
                .uploadedAt(LocalDateTime.now())
                .build();
        repository.save(doc);

        return fileMapper.toFileUploadResponse(doc, text);
    }

    public FileDocument getFileDocument(String fileId) {
        return repository.findById(fileId)
                .orElseThrow(() -> new DocumentNotFoundException("File not found with ID: " + fileId));
    }

    public File getFileAsResource(String fileId) {
        FileDocument doc = getFileDocument(fileId);
        File file = storageService.getFilePath(doc.getStoredFilePath()).toFile();
        if (!file.exists()) {
            throw new DocumentNotFoundException("File not found on storage: " + fileId);
        }
        return file;
    }

    public FileTextResponse getFileText(String fileId) {
        FileDocument doc = getFileDocument(fileId);
        File file = storageService.getFilePath(doc.getStoredFilePath()).toFile();
        if (!file.exists()) {
            throw new DocumentNotFoundException("File not found on storage: " + fileId);
        }
        DocumentExtractionService extractor = extractionFactory.getExtractor(doc.getFileType());
        String text = extractor.extractText(file);
        
        return fileMapper.toFileTextResponse(doc, text);
    }
}
