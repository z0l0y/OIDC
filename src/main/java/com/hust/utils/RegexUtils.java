package com.hust.utils;

import java.util.regex.Pattern;

public class RegexUtils {
    public static boolean validateString(String input) {
        // 定义允许的字符串模式，使用正则表达式
        String pattern = "^(openid|email|profile)(\\s+(openid|email|profile))*$";

        // 使用Pattern类进行正则匹配
        Pattern regex = Pattern.compile(pattern);

        // 进行匹配验证
        return regex.matcher(input).matches();
    }
}
