package com.example.lirarymagement_api.data.model;

import com.example.lirarymagement_api.data.constant.STATUS;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bookId;

  @NotBlank(message = "Title is required")
  private String title;

  @NotBlank(message = "Author is required")
  private String author;

  @NotBlank(message = "ISBN is required")
  @Column(unique = true, nullable = false)
  private String isbn;

  @NotBlank(message = "Cover image URL is required")
  @Column(nullable = false)
  private String coverImageUrl;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class )
  private LocalDateTime timestamp;
  private STATUS status;

  @PrePersist
  protected void onCreate() {
    timestamp = LocalDateTime.now();
  }
}