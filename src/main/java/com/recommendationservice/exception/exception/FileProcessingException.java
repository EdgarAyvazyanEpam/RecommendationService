package com.recommendationservice.exception.exception;

import java.io.Serial;

public class FileProcessingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5297361720781940499L;

    private final String originalFileName;

    public FileProcessingException(String message, String originalFileName) {
        super(message);
        this.originalFileName = originalFileName;
    }

    public FileProcessingException(String message, String originalFileName, Throwable cause) {
        super(message, cause);
        this.originalFileName = originalFileName;
    }

    @Override
    public String getMessage() {
        return String.format("%s : %s", super.getMessage(), this.originalFileName);
    }
}
