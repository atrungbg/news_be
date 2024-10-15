package com.example.NewBackEnd.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {

    private UUID id;
    private String username;
    private String email;
    private String fullName;
    private String address;
    private String roleName;
    private String token;
    private String refreshToken;
    private String status;

}
