package com.example.lirarymagement_api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BorrowBookResponse {
    private String message;
    private Long bookId;
    private Long userId;
    private Long logId;
}