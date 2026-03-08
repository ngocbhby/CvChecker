package com.inovator.CV.Checker.repository;

import com.inovator.CV.Checker.entity.FileDocument;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileDocumentRepository {
    private final ConcurrentHashMap<String, FileDocument> store = new ConcurrentHashMap<>();

    public FileDocument save(FileDocument document) {
        store.put(document.getId(), document);
        return document;
    }

    public Optional<FileDocument> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }
}
