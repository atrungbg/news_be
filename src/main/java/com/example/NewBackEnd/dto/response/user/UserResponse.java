package com.example.NewBackEnd.dto.response.user;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private Date createdDate;
    private String createdBy;
    private String modifiedBy;
    private Date modifiedDate;
    private String status;
    private String username;
    private String email;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String roleName;
}
