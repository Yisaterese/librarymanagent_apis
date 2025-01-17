package com.example.lirarymagement_api.dto.response;

import com.example.lirarymagement_api.data.constant.ACTIVITY;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class LogResponse {
    private Long Id;
    private Long userId ;
    private Long bookId;
    private String description;
    private ACTIVITY action;
    private LocalDateTime timestamp;
}
