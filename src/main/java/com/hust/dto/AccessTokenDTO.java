package com.hust.dto;

import lombok.Data;

@Data
public class AccessTokenDTO {
    private String accessToken;
    private String refreshToken;
}
