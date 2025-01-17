package com.example.lirarymagement_api.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;





public class AddBookRequest {
  private String title;
  private String author;
  private String isbn;
  private MultipartFile coverImageUrl;
  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public String getIsbn() {
    return isbn;
  }

  public MultipartFile getCoverImageUrl() {
    return coverImageUrl;
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

  public void setCoverImageUrl(MultipartFile coverImageUrl) {
    this.coverImageUrl = coverImageUrl;
  }



}
