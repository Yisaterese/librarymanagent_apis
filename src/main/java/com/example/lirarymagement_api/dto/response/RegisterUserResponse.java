 package com.example.lirarymagement_api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterUserResponse {
    private String email;
    private String username;
    private  String message;

}