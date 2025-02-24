package com.example.librarymanagementsystem.dto.response;


import lombok.Getter;

@Getter
public class BooksResponse {
  private Long id;
  private String title;

  public void setId(Long id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public void setCoverImageUrl(String coverImageUrl) {
    this.coverImageUrl = coverImageUrl;
  }

  private String author;
  private String isbn;
  private String coverImageUrl;
}
