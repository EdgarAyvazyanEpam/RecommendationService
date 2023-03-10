package com.recommendationservice.exception.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorTypesConstants {
    INVALID_REQUEST_ERROR("The request could not be processed."),
    INVALID_INPUT_ERROR("The request contains invalid data to perform the operation."),
    INCORRECT_FILE_EXTENSION("Wrong file extension. Please check and upload again"),
    ALREADY_EXISTS("File already processed"),
    INTERNAL_SERVER_ERROR("The server encountered an unexpected condition which prevented it from fulfilling the request."),
    NOT_FOUND_ERROR("The server cannot find the requested resource."),
    IO_ERROR("Could not read the input file"),
    IO_EXCEPTION("IO Exception:");

    private final String errorMessage;
}
