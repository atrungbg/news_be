package com.example.NewBackEnd.service;

import com.example.NewBackEnd.exception.BaseException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface IJWTService {
    String extractUserName(String token) throws BaseException;

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails) throws BaseException;

    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);

    String extractBearerToken(String authorizationHeader);
}
