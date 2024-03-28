package com.hust.utils;

import com.hust.pojo.IDToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IDTokenUtils {
    private static final String signKey = "hust";
    private static final Long expire = 60 * 60 * 1000L;

    //产生jwt令牌
    public static String generateIDToken(Map<String, Object> claims) {
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return jwt;
    }

    //解析令牌
    public static Claims parseIDToken(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }

    public static String createIDToken(String iss, String sub, String aud, Date exp, Date iat, String nonce) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", iss);
        claims.put("sub", sub);
        claims.put("aud", aud);
        claims.put("exp", exp);
        claims.put("iat", iat);
        claims.put("nonce", nonce);
        return generateIDToken(claims);
    }
}
