package com.example.quickdrop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFound(FileNotFoundException ex){
        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(FileExpiredException.class)
    public ResponseEntity<ErrorResponse> handleExpired(FileExpiredException ex){
        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.GONE.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.GONE)
                .body(error);
    }

    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ErrorResponse> handleInvalid(InvalidFileException ex){
        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.badRequest()
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex){

        ex.printStackTrace();

        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.internalServerError()
                .body(error);
    }
}
