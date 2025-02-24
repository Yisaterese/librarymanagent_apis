package com.example.librarymanagementsystem.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.librarymanagementsystem.dto.request.LoginRequest;
import com.example.librarymanagementsystem.dto.response.BaseResponse;
import com.example.librarymanagementsystem.dto.response.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Collection;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authManager;
    private final ObjectMapper mapper = new ObjectMapper();
    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            InputStream requestBodyStream = request.getInputStream();
            LoginRequest loginRequest =mapper.readValue(requestBodyStream, LoginRequest.class);

            String username = loginRequest.getEmail();
            String password = loginRequest.getPassword();
            Authentication authentication = new UsernamePasswordAuthenticationToken(username,password);
            Authentication authenticationResult = authManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authenticationResult);
            return authenticationResult;
        } catch  (IOException e) {
            throw new BadCredentialsException(e.getMessage());
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setMessage("Successful authentication");
        loginResponse.setToken(generateAccessToken(authResult));
        loginResponse.setAuthority(authResult.getAuthorities().toString());
        loginResponse.setEmail(authResult.getPrincipal().toString());
        loginResponse.setRefreshToken(generateRefreshToken(authResult));

        BaseResponse<LoginResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(loginResponse);
        baseResponse.setStatus(true);
        baseResponse.setCode(HttpStatus.OK);
        response.getOutputStream().write(mapper.writeValueAsBytes(baseResponse));
        response.flushBuffer();
        chain.doFilter(request,response);

    }

    private static String generateAccessToken(Authentication authResult) {
        return JWT.create()
                .withIssuer("librarymanagementapi")
                .withClaim("principal",authResult.getPrincipal().toString())
                .withArrayClaim("roles",getClaimsFrom(authResult.getAuthorities()))
                .withExpiresAt(Instant.now().plusSeconds(7 * 24 * 60 * 60))
                .sign(Algorithm.HMAC512("secret"));
    }


    private static String[] getClaimsFrom(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map((grantedAuthority) -> grantedAuthority.getAuthority())
                .toArray(String[]::new);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException exception) throws IOException, ServletException {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setMessage(exception.getMessage());
        BaseResponse<LoginResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(loginResponse);
        baseResponse.setStatus(false);
        baseResponse.setCode(HttpStatus.UNAUTHORIZED);
        response.getOutputStream().write(mapper.writeValueAsBytes(baseResponse));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.flushBuffer();
    }


    private String generateRefreshToken(Authentication authentication) {
        return JWT.create()
                .withIssuer("librarymanagementapi")
                .withClaim("principal",authentication.getPrincipal().toString())
                .withArrayClaim("roles",getClaimsFrom(authentication.getAuthorities()))
                .withExpiresAt(Instant.now().plusSeconds(7 * 24 * 60 * 60))
                .sign(Algorithm.HMAC512("secret"));
    }

}

