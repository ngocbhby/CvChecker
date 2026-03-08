package com.inovator.CV.Checker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedFileTypeException extends FileException {
    public UnsupportedFileTypeException(String message) {
        super(message);
    }
}
