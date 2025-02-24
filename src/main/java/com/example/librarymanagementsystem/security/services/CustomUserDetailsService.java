package com.example.librarymanagementsystem.security.services;

import com.example.librarymanagementsystem.data.model.User;
import com.example.librarymanagementsystem.data.repository.UserRepository;
import com.example.librarymanagementsystem.security.model.SecuredUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("user not found"));
        return new SecuredUser(foundUser);
    }
}
