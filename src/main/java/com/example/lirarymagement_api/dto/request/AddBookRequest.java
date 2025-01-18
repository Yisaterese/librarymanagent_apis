package com.example.lirarymagement_api.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;




public class AddBookRequest {
  private String title;
  private String author;
  private String isbn;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public MultipartFile getCoverImageUrl() {
    return coverImageUrl;
  }

  public void setCoverImageUrl(MultipartFile coverImageUrl) {
    this.coverImageUrl = coverImageUrl;
  }

  private MultipartFile coverImageUrl;




}
