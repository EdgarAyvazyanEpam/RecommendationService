package com.recommendationservice.exception.exception;

import java.io.Serial;

public class InvalidFileFormatException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4238166109105690160L;
    public InvalidFileFormatException(String message) {
        super(message);
    }
}
