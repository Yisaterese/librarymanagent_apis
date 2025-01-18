package com.example.lirarymagement_api.handler;

import com.example.lirarymagement_api.exception.LibraryManagementException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LibraryManagementException.class)
    public ResponseEntity<?> handleLibrarymanagementException(LibraryManagementException exception) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(Map.of(
                        "error", exception.getMessage(),
                        "success", false));
    }

    // Handle IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(Map.of(
                        "error", exception.getMessage(),
                        "success", false));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException exception) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", exception.getMessage(),
                        "success", false));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException exception) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", exception.getMessage(),
                        "success", false));
    }
}
