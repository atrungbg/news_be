package com.example.NewBackEnd.service;

import com.example.NewBackEnd.dto.request.user.CreateUserRequest;
import com.example.NewBackEnd.dto.request.user.LoginRequest;
import com.example.NewBackEnd.dto.request.user.UpdateUserRequest;
import com.example.NewBackEnd.dto.response.JwtAuthenticationResponse;
import com.example.NewBackEnd.dto.response.user.UserResponse;
import com.example.NewBackEnd.exception.BaseException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

public interface IUserService extends IGenericService<UserResponse> {
    JwtAuthenticationResponse login(LoginRequest loginRequest) throws BaseException;
    JwtAuthenticationResponse create(CreateUserRequest createUserRequest) throws BaseException;
    UserResponse updateUser(UUID userId, UpdateUserRequest updateUserRequest) throws BaseException;
    UserResponse loginGoogle(String token) throws BaseException;
}
