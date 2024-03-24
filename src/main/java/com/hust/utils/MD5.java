package com.hust.utils;

import com.hust.dto.UserDTO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static void generatePasswordMD5(UserDTO userDTO) throws NoSuchAlgorithmException {
        // 如果匹配成功的处理逻辑
        String password = userDTO.getPassword();
        MessageDigest md = null;
        md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(password.getBytes());
        // 将字节数组转换为符号表示
        StringBuilder sb = new StringBuilder();
        // 用StringBuilder，避免造成字符池的资源浪费
        for (byte b : messageDigest) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
    }
}
