package com.recommendationservice.exception.exception;

import java.io.Serial;

public class InvalidUploadedFileException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5460185325774700620L;

    public InvalidUploadedFileException(String message) {
        super(message);
    }
}

