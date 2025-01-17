package com.example.lirarymagement_api.dto.response;


import lombok.Getter;

@Getter
public class DeleteBookResponse {

  public void setMessage(String message) {
    this.message = message;
  }

  private String message;
}
