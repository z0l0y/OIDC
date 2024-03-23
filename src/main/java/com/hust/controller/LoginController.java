package com.hust.controller;

import com.hust.utils.JwtUtils;
import com.hust.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
//
//    @PostMapping("/login")
//    public Result login(@RequestBody Trueuser trueuser){
//        Trueuser e= trueUserService.login(trueuser);
//        //登录成功，生成令牌，下发令牌
//        if(e!=null){
//            Map<String,Object>claims=new HashMap<>();
//            claims.put("username",e.getUsername());
//            claims.put("password",e.getPassword());
//
//            String token= JwtUtils.generateJwt(claims);
//
//            return Result.success(token);
//        }
//        //登录失败，放回错误信息
//        return  Result.error("用户名或密码错误");
//    }

}
/*         claims.put("id",e.getId());*/