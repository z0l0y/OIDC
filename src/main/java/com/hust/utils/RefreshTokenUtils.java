package com.hust.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class RefreshTokenUtils {
    private static final String signKey = "hust";
    // 这里其实还可以优化一下，建议直接算出来，要不然程序每次生成token的时候都要进行计算，会浪费一部分的性能
    private static final Long expire = 60 * 60 * 24 * 30 * 3L;

    //产生jwt令牌
    public static String generateRefreshToken(Map<String, Object> claims) {
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return jwt;
    }

    //解析令牌
    public static Claims parseRefreshToken(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }
}
