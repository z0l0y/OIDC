package com.hust.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import java.util.Date;

public class ExamineTokenExpire {
    public static Result examineToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey("hust")
                .parseClaimsJws(token)
                .getBody();

        // 获取过期时间
        Date expirationDate = claims.getExpiration();

        // 检查是否过期
        boolean isExpired = expirationDate.getTime() < System.currentTimeMillis();

        if (isExpired) {
            return Result.error("JWT 令牌已过期");
        } else {
            return Result.success("JWT 令牌仍然有效");
        }
    }
}
