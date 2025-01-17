package com.example.lirarymagement_api.dto.request;


import lombok.Getter;

public class GetBookRequest {
  private Long id;

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  private String title;
  private String isbn;
  
}
