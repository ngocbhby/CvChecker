package com.inovator.CV.Checker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DocumentNotFoundException extends FileException {
    public DocumentNotFoundException(String message) {
        super(message);
    }
}
