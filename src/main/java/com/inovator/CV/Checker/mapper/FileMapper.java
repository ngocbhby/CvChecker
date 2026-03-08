package com.inovator.CV.Checker.mapper;

import com.inovator.CV.Checker.dto.FileTextResponse;
import com.inovator.CV.Checker.dto.FileUploadResponse;
import com.inovator.CV.Checker.entity.FileDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper {

    @Mapping(target = "fileId", source = "document.id")
    @Mapping(target = "fileName", source = "document.originalFileName")
    @Mapping(target = "filePath", source = "document.storedFilePath")
    @Mapping(target = "text", source = "extractedText")
    FileUploadResponse toFileUploadResponse(FileDocument document, String extractedText);

    @Mapping(target = "fileId", source = "document.id")
    @Mapping(target = "text", source = "extractedText")
    FileTextResponse toFileTextResponse(FileDocument document, String extractedText);
}
