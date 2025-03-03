package com.example.librarymanagementsystem.security.config;

import com.example.librarymanagementsystem.data.constant.ROLE;
import com.example.librarymanagementsystem.security.filters.CustomAuthorizationFilter;
import com.example.librarymanagementsystem.security.filters.CustomUsernamePasswordAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationManager authenticationManager;

    private final CustomAuthorizationFilter customAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var authenticationFilter = new CustomUsernamePasswordAuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl("/api/auth");

        return http.csrf(c -> c.disable())
                .cors(c -> c.disable())
                .sessionManagement(c -> c.sessionCreationPolicy(STATELESS))
                .addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customAuthorizationFilter, CustomUsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/api/auth").permitAll()
                        .requestMatchers("/api/users/register").permitAll()
                        .requestMatchers("/api/book/get-books").permitAll()
                        .requestMatchers("/api/book/update-book/{id}").hasAuthority(ROLE.ADMIN.toString())
                        .requestMatchers("/api/book/delete-book/{id}").hasAuthority(ROLE.ADMIN.toString())
                        .requestMatchers("/api/users/add_book").hasAuthority(ROLE.ADMIN.toString())
                        .requestMatchers("/api/users/delete_user").hasAuthority(ROLE.ADMIN.toString())
                        .requestMatchers("/api/users/assign-role/{userId}").hasAuthority(ROLE.ADMIN.toString())
                        .anyRequest().authenticated()

                ).build();
    }
}
