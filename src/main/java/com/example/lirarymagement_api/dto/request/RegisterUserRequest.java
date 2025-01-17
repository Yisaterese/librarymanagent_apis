package com.example.lirarymagement_api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterUserRequest {
    private String username;
    private String password;
    private String email;
}
