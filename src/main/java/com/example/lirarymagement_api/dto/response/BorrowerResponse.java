package com.example.lirarymagement_api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BorrowerResponse {
    private Long borrowerId;
    private String message;
}