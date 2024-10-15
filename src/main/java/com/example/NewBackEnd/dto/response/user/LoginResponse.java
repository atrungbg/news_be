package com.example.NewBackEnd.dto.response.user;

import lombok.Data;

import java.time.Instant;

@Data
public class LoginResponse {
    private int id;
    private String username;
    private String email;
    private String fullName;
    private String address;
    private String role;
    private String token;
    private String refreshToken;
    private Instant expiredTime;
}
