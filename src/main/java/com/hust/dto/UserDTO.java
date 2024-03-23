package com.hust.dto;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private String username;

    private String password;

    private String email;

    private String nickname;

    private String avatar;

    private String bio;
}
