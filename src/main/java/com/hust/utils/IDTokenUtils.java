package com.hust.utils;

import com.hust.pojo.IDToken;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IDTokenUtils {
    private static final String signKey = "hust";
    private static final SecretKeySpec secretKey = generateSecretKey();
    private static final Long expire = 60 * 60 * 1000L;

    private static SecretKeySpec generateSecretKey() {
        // 生成长度为 16 字节的密钥
        byte[] keyBytes = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(keyBytes);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        return secretKeySpec;
    }

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

    // 产生JWE令牌
    public static String generateJWEToken(Map<String, Object> claims, JWEHeader jweHeader, JWEEncrypter encrypter) throws JOSEException {
        String jwt = generateIDToken(claims); // 生成JWT令牌

        Payload payload = new Payload(jwt);
        JWEObject jweObject = new JWEObject(jweHeader, payload);
        jweObject.encrypt(encrypter);

        return jweObject.serialize();
    }

    // 创建JWE令牌
    public static String createJWEToken(String iss, String sub, String aud, Date exp, Date iat, String nonce) throws JOSEException {
        JWEHeader jweHeader = new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128GCM)
                .contentType("JWT")
                .keyID("key123")
                .build();
        JWEEncrypter encrypter = new DirectEncrypter(secretKey);
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", iss);
        claims.put("sub", sub);
        claims.put("aud", aud);
        claims.put("exp", exp);
        claims.put("iat", iat);
        claims.put("nonce", nonce);

        // 返回生成的JWE令牌
        return generateJWEToken(claims, jweHeader, encrypter);
    }

    // 解密JWE
    public static String decryptJWEToken(String jweToken) throws JOSEException, ParseException {
        JWEDecrypter decrypter = new DirectDecrypter(secretKey);
        JWEObject jweObject = JWEObject.parse(jweToken);
        jweObject.decrypt(decrypter);
        return jweObject.getPayload().toString();
    }
}
