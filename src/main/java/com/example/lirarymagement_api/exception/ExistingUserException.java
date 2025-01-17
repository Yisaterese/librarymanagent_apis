package com.example.lirarymagement_api.exception;


public class ExistingUserException extends LibraryManagementException {
    public ExistingUserException(String message) {
        super(message);
    }
}
