package com.hust.dto;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 16, message = "Username must be between 4 and 16 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Nickname cannot be blank")
    @Size(max = 16, message = "Nickname must not exceed 16 characters")
    private String nickname;

    @URL(message = "Invalid avatar URL")
    private String avatar;

    @Size(max = 64, message = "Bio must not exceed 64 characters")
    private String bio;
}
