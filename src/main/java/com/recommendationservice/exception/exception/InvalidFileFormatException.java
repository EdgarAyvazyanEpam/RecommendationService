package com.recommendationservice.exception.exception;

import lombok.experimental.StandardException;

@StandardException
public class InvalidFileFormatException extends RuntimeException {
    public InvalidFileFormatException(String message) {
        super(message);
    }
}
