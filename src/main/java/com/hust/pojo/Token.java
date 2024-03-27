package com.hust.pojo;

import lombok.Data;

@Data
public class Token {
    private String code;
    private String accessToken;
    private String refreshToken;
}
