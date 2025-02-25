package com.example.librarymanagementsystem.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBookResponse {
  private Long id;
  private String title;
  private String author;
  private String isbn;
  private String message;
}
