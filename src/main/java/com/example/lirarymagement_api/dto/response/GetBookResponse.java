package com.example.lirarymagement_api.dto.response;


import lombok.Getter;

@Getter
public class GetBookResponse {
  private Long id;

  public void setId(Long id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  private String title;
  private String author;
  private String message;
}
