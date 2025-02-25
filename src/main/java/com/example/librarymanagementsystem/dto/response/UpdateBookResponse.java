package com.example.librarymanagementsystem.dto.response;


import lombok.Getter;

@Getter
public class UpdateBookResponse {
  private String id;
  private String message;;

  public void setId(String id) {
    this.id = id;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
