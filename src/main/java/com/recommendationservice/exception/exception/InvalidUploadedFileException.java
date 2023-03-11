package com.recommendationservice.exception.exception;

import lombok.experimental.StandardException;

@StandardException
public class InvalidUploadedFileException extends RuntimeException {
    public InvalidUploadedFileException(String message) {
        super(message);
    }
}

