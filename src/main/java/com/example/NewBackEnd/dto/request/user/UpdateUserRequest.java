package com.example.NewBackEnd.dto.request.user;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

  @Size(max = 100, message = "Username must be less than or equal to 100 characters")
  private String username;

  @Size(min = 8, max = 80, message = "Password must be less than or equal to 80 characters")
  private String password;

  private String email;

  private String roleName;

  private String phoneNumber;

  private String fullName;  // Thêm trường cho tên đầy đủ nếu cần

  private String address;    // Thêm trường cho địa chỉ nếu cần
}
