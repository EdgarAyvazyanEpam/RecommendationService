package com.recommendationservice.exception.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorConstants {
    INVALID_REQUEST_ERROR_TITLE("The request could not be processed."),

    INVALID_INPUT_ERROR_TITLE("The request contains invalid data to perform the operation."),

    INTERNAL_SERVER_ERROR_TITLE("The server encountered an unexpected condition which prevented it from fulfilling the request."),

    NOT_FOUND_ERROR_TITLE("The server cannot find the requested resource."),

    IO_ERROR_TITLE("Could not read the input file"),

    ALREADY_EXISTS("File already processed"),
    UNAUTHORIZED_ERROR_TITLE("The request requires client authentication."),
    INCORRECT_FILE_EXTENSION("Wrong file extension. Please check and upload again"),
    IO_EXCEPTION("IO Exception:");

    private final String errorMessage;
}
