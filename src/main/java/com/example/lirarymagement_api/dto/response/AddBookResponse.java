package com.example.lirarymagement_api.dto.response;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class AddBookResponse {
  private String message;
  private Long id;
  private String isbn;
  private String coverImageUrl;

  private LocalDateTime timestamp;

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public String getMessage() {
    return message;
  }

  public Long getId() {
    return id;
  }

  public String getIsbn() {
    return isbn;
  }

  public String getCoverImageUrl() {
    return coverImageUrl;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public void setCoverImageUrl(String coverImageUrl) {
    this.coverImageUrl = coverImageUrl;
  }



}
