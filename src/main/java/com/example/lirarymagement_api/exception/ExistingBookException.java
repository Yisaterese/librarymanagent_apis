package com.example.lirarymagement_api.exception;

public class ExistingBookException extends LibraryManagementException {
    public ExistingBookException(String message) {
        super(message);
    }
}
