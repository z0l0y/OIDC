package com.hust.pojo;

import lombok.Data;

@Data
public class IDToken {
    private String iss;
    private String sub;
    private String aud;
    private String exp;
    private String iat;
    private String nonce;

}
