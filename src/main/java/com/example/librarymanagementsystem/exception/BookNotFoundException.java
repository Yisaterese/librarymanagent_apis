package com.example.librarymanagementsystem.exception;

public class BookNotFoundException extends LibraryManagementException {
  public BookNotFoundException(String message){
    super(message);
  }
}
