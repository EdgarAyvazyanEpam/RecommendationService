package com.recommendationservice.exception.exception;

import java.io.Serial;

public class CSVParseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7819403791835709422L;
    private final String originalFileName;

    public CSVParseException(String message, String originalFileName) {
        super(message);
        this.originalFileName = originalFileName;
    }

    public CSVParseException(String message, String originalFileName, Throwable cause) {
        super(message, cause);
        this.originalFileName = originalFileName;
    }

    @Override
    public String getMessage() {
        return String.format("%s : %s", super.getMessage(), this.originalFileName);
    }
}
