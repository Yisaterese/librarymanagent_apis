package com.example.librarymanagementsystem.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.example.librarymanagementsystem.security.utils.SecurityUtils.JWT_PREFIX;
import static com.example.librarymanagementsystem.security.utils.SecurityUtils.PUBLIC_ENDPOINT;

@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getServletPath();
        boolean isRequestPathPublic = PUBLIC_ENDPOINT.contains(requestPath);
        if(isRequestPathPublic) filterChain.doFilter(request,response);
        String authorizationRequest = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorizationRequest != null) {
            String token = authorizationRequest.substring(JWT_PREFIX.length()).strip();
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512("secret".getBytes()))
                    .withIssuer("librarymanagementsystem")
                    .withClaimPresence("roles")
                    .withClaimPresence("principal")
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);

            List<SimpleGrantedAuthority> authorities = decodedJWT.getClaim("roles").asList(SimpleGrantedAuthority.class);
            JsonNode userDetails = objectMapper.convertValue(decodedJWT.getClaim("principal"),JsonNode.class);
            System.out.println(userDetails);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

}
