package com.example.NewBackEnd.dto.response.role;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse {
    private UUID id;

    private Date createdDate;

    private Date modifiedDate;

    private String createdBy;

    private String modifiedBy;

    private String roleName;


    private String description;

    private String status;


}
