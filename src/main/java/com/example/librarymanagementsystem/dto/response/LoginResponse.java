package com.example.librarymanagementsystem.dto.response;

import com.example.librarymanagementsystem.data.constant.ROLE;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String message;
    private String token;
    private String refreshToken;
    private String authority;
    private String email;

}
