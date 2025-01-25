package com.example.lirarymagement_api.data.model;

import com.example.lirarymagement_api.data.constant.ROLE;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;
    @NotNull
    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format. Please provide a valid email address.")
    private String email;
    private String username;
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{7,}$", message = "Password must be at least 7 characters long, contain at least one letter, one number, and one special character")
    private String password;
    @Enumerated(EnumType.STRING)
    private Set<ROLE> roles;
    private LocalDateTime time;


    @PrePersist
    protected void onCreate() {
        time = LocalDateTime.now();
    }

}
