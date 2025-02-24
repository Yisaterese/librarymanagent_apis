package com.example.librarymanagementsystem.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Getter
@Setter
public class ValidatorService {
    private HashMap<String, String> userCredentials = new HashMap<>();
    public void validateUsernamePassword(String username, String password){
     userCredentials.put(username,password);
    }
}
