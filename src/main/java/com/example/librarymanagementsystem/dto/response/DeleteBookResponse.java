package com.example.librarymanagementsystem.dto.response;


import lombok.Getter;

@Getter
public class DeleteBookResponse {

  public void setMessage(String message) {
    this.message = message;
  }

  private String message;
}
