package com.hust.dto;

import lombok.Data;

@Data
public class AuthorizeDTO {
    private String response_type;
    private String client_id;
    private String redirect_url;
    private String scope;
    private String state;
}
