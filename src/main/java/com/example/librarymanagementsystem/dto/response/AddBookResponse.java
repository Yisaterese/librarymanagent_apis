package com.example.librarymanagementsystem.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddBookResponse {
    private String message;
    private Long bookId;
    private Long logId;

}
