package com.inovator.CV.Checker.service.storage;

import com.inovator.CV.Checker.exception.FileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.YearMonth;

@Service
public class LocalFileStorageService {

    private final Path uploadDir;

    public LocalFileStorageService(@Value("${app.storage.upload-dir:./uploads}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (Exception ex) {
            throw new FileException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file, String fileId) {
        try {
            YearMonth currentYearMonth = YearMonth.now();
            String yearMonthPath = currentYearMonth.getYear() + "/" + String.format("%02d", currentYearMonth.getMonthValue());
            Path targetLocationDir = this.uploadDir.resolve(yearMonthPath);
            
            Files.createDirectories(targetLocationDir);
            
            String originalFileName = file.getOriginalFilename();
            String extension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            
            String storedFileName = fileId + extension;
            Path targetLocation = targetLocationDir.resolve(storedFileName);
            
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();
        } catch (IOException ex) {
            throw new FileException("Could not store file " + file.getOriginalFilename() + ". Please try again!", ex);
        }
    }

    public Path getFilePath(String storedFilePath) {
        return Paths.get(storedFilePath).toAbsolutePath().normalize();
    }
}
