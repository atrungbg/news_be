package com.example.NewBackEnd.controller;

import com.example.NewBackEnd.constant.ConstAPI;
import com.example.NewBackEnd.dto.request.user.LoginRequest;
import com.example.NewBackEnd.dto.response.JwtAuthenticationResponse;
import com.example.NewBackEnd.exception.BaseException;
import com.example.NewBackEnd.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@Slf4j
@Tag(name = "User Controller")
public class UserController {
    @Autowired
    private IUserService userService;

    @Operation(summary = "Login", description = "API login ")
    @PostMapping(value = ConstAPI.AuthenticationAPI.LOGIN_WITH_PASSWORD_USERNAME)
    public JwtAuthenticationResponse login(@RequestBody LoginRequest request) throws BaseException {
        log.info("Creating new account with request: {}", request);
        return userService.login(request);
    }

}
