package com.example.librarymanagementsystem.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class AddBookRequest {
  private Long userId;
  private String title;
  private String author;
  private String isbn;
  private String description;

}
