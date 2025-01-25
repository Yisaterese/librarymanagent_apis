package com.example.lirarymagement_api.security.services;

import com.example.lirarymagement_api.data.model.User;
import com.example.lirarymagement_api.data.repository.UserRepository;
import com.example.lirarymagement_api.exception.UserNotFoundException;
import com.example.lirarymagement_api.security.model.SecuredUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private  final UserRepository user;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = user.findByEmail(username).orElseThrow(()-> new UserNotFoundException("User not found."));
        return new SecuredUser(foundUser);
    }
}
