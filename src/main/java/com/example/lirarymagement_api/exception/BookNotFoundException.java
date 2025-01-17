package com.example.lirarymagement_api.exception;

public class BookNotFoundException extends LibraryManagementException {
  public BookNotFoundException(String message){
    super(message);
  }
}
