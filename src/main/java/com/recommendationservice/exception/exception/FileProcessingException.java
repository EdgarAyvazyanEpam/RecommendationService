package com.recommendationservice.exception.exception;

import lombok.experimental.StandardException;

@StandardException
public class FileProcessingException extends RuntimeException {
    public FileProcessingException(String message) {
        super(message);
    }
}
