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
import java.security.SignatureException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IDTokenUtils {
    private static final String signKey = "hust";
    private static final String secretKey = "husthusthusthust";
    private static final Long expire = 1000L;

    // 60 * 60 * 1000

    private static SecretKeySpec generateSecretKey(String secretKey) {
        // 将字符串密钥转换为字节数组
        byte[] keyBytes = secretKey.getBytes();

        // 使用字节数组密钥创建SecretKeySpec对象
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        return secretKeySpec;
    }


    /*private static SecretKeySpec generateSecretKey(String secretKey) {

        // 将字符串密钥转换为字节数组
        byte[] keyBytes = secretKey.getBytes();

        // 使用字节数组密钥创建SecretKeySpec对象
        SecretKeySpec secretKey1 = new SecretKeySpec(keyBytes, "AES");

        // 使用SecretKeySpec对象创建DirectDecrypter对象
        try {
            JWEDecrypter decrypter = new DirectDecrypter(secretKey1);
        } catch (KeyLengthException e) {
            e.printStackTrace();
        }

*//*        // 生成长度为 16 字节的密钥
        byte[] keyBytes = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(keyBytes);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        System.out.println("secretKeySpec： " + secretKeySpec);*//*
        return secretKeySpec;
    }
*/
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
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("正在解析JWT令牌");
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
        System.out.println("iss: " + claims.get("iss"));
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
    public static String createJWEToken(String iss, String sub, String aud, Date exp, Date iat, String nonce, String picture, String nickname, String name, String email) throws JOSEException {
        JWEHeader jweHeader = new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128GCM)
                .contentType("JWT")
                .build();
        SecretKeySpec secretKeySpec = generateSecretKey(secretKey);
        JWEEncrypter encrypter = new DirectEncrypter(secretKeySpec);
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", iss);
        claims.put("sub", sub);
        claims.put("aud", aud);
        claims.put("exp", exp);
        claims.put("iat", iat);
        claims.put("name", name);
        claims.put("picture", picture);
        claims.put("nickname", nickname);
        claims.put("email", email);
        claims.put("nonce", nonce);

        // 返回生成的JWE令牌
        return generateJWEToken(claims, jweHeader, encrypter);
    }

    // 解密JWE
    public static String decryptJWEToken(String jweToken) throws KeyLengthException, ParseException {
        SecretKeySpec secretKeySpec = generateSecretKey(secretKey);
        JWEDecrypter decrypter = new DirectDecrypter(secretKeySpec);
        JWEObject jweObject = JWEObject.parse(jweToken);
        try {
            jweObject.decrypt(decrypter);
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return jweObject.getPayload().toString();
    }
}
