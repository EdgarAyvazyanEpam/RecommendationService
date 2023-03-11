package com.recommendationservice.exception.exception;

import lombok.experimental.StandardException;

@StandardException
public class CryptoValueNotFoundException extends RuntimeException {
    public CryptoValueNotFoundException() {
    }

    public CryptoValueNotFoundException(String message) {
        super(message);
    }

    public CryptoValueNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
