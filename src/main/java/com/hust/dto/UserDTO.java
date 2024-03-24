package com.hust.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDTO {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 1, max = 16, message = "Username must be between 1 and 16 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Nickname cannot be blank")
    @Size(min = 1, max = 16, message = "Nickname must be between 1 and 16 characters")
    private String nickname;
}
