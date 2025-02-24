package com.example.librarymanagementsystem.handler;

import com.example.librarymanagementsystem.exception.LibraryManagementException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LibraryManagementException.class)
    public ResponseEntity<?> handleLibrar21anagementException(LibraryManagementException exception) {
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

    @ExceptionHandler
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException exception){
        return  ResponseEntity.status(UNAUTHORIZED)
                .body(Map.of(
                        "error", "not authorized",
                        "success", false
                ));
    }
}
