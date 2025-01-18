package com.example.lirarymagement_api.exception;

public class UserNotFoundException extends LibraryManagementException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
