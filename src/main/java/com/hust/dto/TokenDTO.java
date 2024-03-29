package com.hust.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDTO {
    private String grant_type;
    private String code;
    private String redirect_uri;
    private String client_id;
    private String client_secret;
}
