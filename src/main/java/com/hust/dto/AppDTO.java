package com.hust.dto;

import lombok.Data;

@Data
public class AppDTO {
    private String name;
    private String clientSecret;
    private String redirectUrl;
}
