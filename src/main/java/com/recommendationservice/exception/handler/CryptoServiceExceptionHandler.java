package com.recommendationservice.exception.handler;

import com.recommendationservice.exception.enumeration.ErrorConstants;
import com.recommendationservice.exception.enumeration.ErrorType;
import com.recommendationservice.exception.exception.CSVParseException;
import com.recommendationservice.exception.exception.CryptoValueNotFoundException;
import com.recommendationservice.exception.exception.FileProcessingException;
import com.recommendationservice.exception.exception.InvalidFileFormatException;
import com.recommendationservice.exception.response.ErrorResponseDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@Slf4j
@ControllerAdvice
public class CryptoServiceExceptionHandler {


    @ExceptionHandler(value = {FileProcessingException.class})
    public ResponseEntity<ErrorResponseDto> handleIOException(FileProcessingException ex) {
        log.error(ErrorConstants.IO_EXCEPTION + ex.getMessage(), ex);
        ErrorResponseDto errorResponse = new ErrorResponseDto(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                ErrorConstants.ALREADY_EXISTS.getErrorMessage(), ex.getMessage(), LocalDateTime.now().toString(),
                ErrorType.INVALID_REQUEST_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidFileFormatException.class})
    public ResponseEntity<ErrorResponseDto> handleIOException(InvalidFileFormatException ex) {
        log.error(ErrorConstants.IO_EXCEPTION + ex.getMessage(), ex);
        ErrorResponseDto errorResponse = new ErrorResponseDto(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                ErrorConstants.INCORRECT_FILE_EXTENSION.getErrorMessage(), ex.getMessage(), LocalDateTime.now().toString(),
                ErrorType.INVALID_REQUEST_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {CryptoValueNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleCryptoValueNotFoundException(CryptoValueNotFoundException ex) {
        log.error(String.format("Crypto Value Not Found Exception: %s", ex.getMessage()), ex);
        final HttpStatus notFound = HttpStatus.NOT_FOUND;

        ErrorResponseDto errorResponse = new ErrorResponseDto(String.valueOf(notFound.value()),
                ErrorConstants.NOT_FOUND_ERROR_TITLE.getErrorMessage(), ex.getMessage(), LocalDateTime.now().toString(),
                ErrorType.INVALID_REQUEST_ERROR);
        return new ResponseEntity<>(errorResponse, notFound);
    }

    @ExceptionHandler(value = {IOException.class})
    public ResponseEntity<ErrorResponseDto> handleIOException(IOException ex) {
        log.error(ErrorConstants.IO_EXCEPTION + ex.getMessage(), ex);
        ErrorResponseDto errorResponse = new ErrorResponseDto(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                ErrorConstants.IO_ERROR_TITLE.getErrorMessage(), ex.getMessage(), LocalDateTime.now().toString(),
                ErrorType.INVALID_REQUEST_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(String.format("Method Argument Not Valid Exception: %s", ex.getMessage()), ex);
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        List<String> messages = ex.getBindingResult().getFieldErrors()
            .stream()
            .map(fieldError -> String.format("%s %s", fieldError.getField(),
                fieldError.getDefaultMessage())).toList();


        ErrorResponseDto errorResponse = new ErrorResponseDto(String.valueOf(badRequest.value()),
                ErrorConstants.INVALID_INPUT_ERROR_TITLE.getErrorMessage(), messages.toString(), LocalDateTime.now().toString(),
                ErrorType.INVALID_REQUEST_ERROR);
        return new ResponseEntity<>(errorResponse, badRequest);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error(String.format("Constraint Violation Exception: %s", ex.getMessage()), ex);
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        List<String> messages = ex.getConstraintViolations()
            .stream()
            .map(
                constraintViolation -> String.format("%s %s", constraintViolation.getPropertyPath(),
                    constraintViolation.getMessage())).toList();

        ErrorResponseDto errorResponse = new ErrorResponseDto(String.valueOf(badRequest.value()),
                ErrorConstants.INVALID_INPUT_ERROR_TITLE.getErrorMessage(), messages.toString(), LocalDateTime.now().toString(),
                ErrorType.INVALID_REQUEST_ERROR);
        return new ResponseEntity<>(errorResponse, badRequest);
    }

    @ExceptionHandler(value = {CSVParseException.class})
    public ResponseEntity<ErrorResponseDto> handleCsvExceptionException(CSVParseException ex) {
        String message = String.format("%s", ex.getMessage());
        log.error("Csv Required Field Empty Exception: " + message, ex);

        ErrorResponseDto errorResponse = new ErrorResponseDto(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                ErrorConstants.IO_ERROR_TITLE.getErrorMessage(), message, LocalDateTime.now().toString(),
                ErrorType.INVALID_REQUEST_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
