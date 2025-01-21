package com.example.lirarymagement_api.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();

    }
}
