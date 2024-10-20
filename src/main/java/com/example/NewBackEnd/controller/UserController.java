package com.example.NewBackEnd.controller;

import com.example.NewBackEnd.constant.ConstAPI;
import com.example.NewBackEnd.dto.request.user.CreateUserRequest;
import com.example.NewBackEnd.dto.request.user.LoginRequest;
import com.example.NewBackEnd.dto.request.user.UpdateUserRequest;
import com.example.NewBackEnd.dto.response.JwtAuthenticationResponse;
import com.example.NewBackEnd.dto.response.user.UserResponse;
import com.example.NewBackEnd.exception.BaseException;
import com.example.NewBackEnd.model.PagingModel;
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

    @Operation(summary = "Login with google", description = "API login with google")
    @PostMapping(value = ConstAPI.AuthenticationAPI.LOGIN_WITH_GOOGLE)
    public UserResponse loginGoogle(@RequestParam("token") String token) throws BaseException {
        return userService.loginGoogle(token);
    }

    @Operation(summary = "Create account", description = "API create new account")
    @PostMapping(value = ConstAPI.UserAPI.CREATE_ACCOUNT/*, consumes = MediaType.MULTIPART_FORM_DATA_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE*/)
    public JwtAuthenticationResponse create(@Valid @RequestBody CreateUserRequest createUserRequest) throws BaseException {
        return userService.create(createUserRequest);
    }

    @Operation(summary =  "Get account by id", description = "API get account by id")
    @GetMapping(value = ConstAPI.UserAPI.GET_ACCOUNT_BY_ID + "{id}")
    public UserResponse findById(@PathVariable("id") UUID id) throws BaseException {
        return userService.findById(id);
    }

    @Operation(summary =  "Get all user", description = "API get all user")
    @GetMapping(value = ConstAPI.UserAPI.GET_ALL_ACCOUNT)
    public PagingModel<UserResponse> getAll(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return userService.getAll(page, limit);
    }

    @Operation(summary =  "Get all user", description = "API get all user")
    @GetMapping(value = ConstAPI.UserAPI.GET_ALL_ACCOUNT_ACTIVE)
    public PagingModel<UserResponse> getAllByStatusIsActive(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return userService.findAllByStatusTrue(page, limit);
    }

    @Operation(summary = "Update user", description = "API update user")
    @PatchMapping(value = ConstAPI.UserAPI.UPDATE_USER + "/{id}")
    public UserResponse updateUser(@PathVariable("userId") UUID userId, @Valid @RequestBody UpdateUserRequest updateUserRequest) throws BaseException {
        return userService.updateUser(userId, updateUserRequest);
    }

}
