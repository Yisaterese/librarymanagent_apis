package com.example.lirarymagement_api.exception;

import lombok.Getter;
import org.apache.tomcat.jni.LibraryNotFoundError;
import org.springframework.http.HttpStatus;
@Getter
public class LibraryManagementException extends RuntimeException {
    public LibraryManagementException(String message) {
        super(message);
    }
}