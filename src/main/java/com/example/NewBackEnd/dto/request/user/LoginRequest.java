package com.example.NewBackEnd.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "username must not be blank")
    @NotNull(message = "username must not be null")
    private String emailOrUsername;
    private String password;
}
