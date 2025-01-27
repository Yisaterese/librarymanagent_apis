package com.example.lirarymagement_api.security.config;

import com.example.lirarymagement_api.data.constant.ROLE;
import com.example.lirarymagement_api.security.filters.CustomAuthorizationFilter;
import com.example.lirarymagement_api.security.filters.CustomUsernamePasswordAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
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
        return http.csrf(c->c.disable())
                .cors(c->c.disable())
                .sessionManagement(c->c.sessionCreationPolicy(STATELESS))
                .addFilterAt(authenticationFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(customAuthorizationFilter,CustomUsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(c->c.requestMatchers("/api/auth").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/users/add_book").hasRole(ROLE.ADMIN.toString())
                        .requestMatchers("/api/users/register").permitAll()
                        .requestMatchers("/api/book/**").permitAll()
                ).build();
    }

}
