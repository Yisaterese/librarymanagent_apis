package com.example.lirarymagement_api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class  UserResponse {
    private Long id;
    private String username;
    private String email;
    private String message;

}