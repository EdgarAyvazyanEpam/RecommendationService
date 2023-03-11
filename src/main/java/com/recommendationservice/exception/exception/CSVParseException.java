package com.recommendationservice.exception.exception;

import lombok.experimental.StandardException;

@StandardException
public class CSVParseException extends RuntimeException {
    public CSVParseException(String message) {
        super(message);
    }
}
