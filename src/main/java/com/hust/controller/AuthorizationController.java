package com.hust.controller;

import com.hust.dto.AppDTO;
import com.hust.dto.AuthorizeDTO;
import com.hust.service.AuthorizationService;
import com.hust.utils.CodeUtils;
import com.hust.utils.JwtUtils;
import com.hust.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class AuthorizationController {
    @Autowired
    private AuthorizationService authorizationService;

    private static final String AUTHORIZATION_SUCCESS = "申请授权第三方成功！";

    @PostMapping("/settings/apps/new")
    public Result registerOAuth2(@RequestBody AppDTO appDTO) {
        Result isCreateApp = authorizationService.createApp(appDTO);
        if (isCreateApp.getCode() == 1) {
            // 返回唯一的一个client_id，给开发者，client_secret由开发者自己设置
            String clientId = (String) isCreateApp.getData();
            return Result.success(clientId);
        } else {
            return Result.error("注册app失败!");
        }
    }

/*    @PostMapping("/")
    public Result getAccessToken(@RequestBody ){

    }*/

    @PostMapping("/login/oauth/authorize")
    public Result authorizeMyApp(@RequestBody AuthorizeDTO authorizeDTO) {
        Result authorize = authorizationService.authorize(authorizeDTO);
        if (authorize.getCode() == 1) {
            //登录成功，生成令牌，下发令牌
            Map<String, Object> claims = new HashMap<>();
            String uuidCode = UUID.randomUUID().toString().replace("-", "");
            claims.put("code", uuidCode);
            String code = CodeUtils.generateCode(claims);
            String Base64Code = Base64.getEncoder().encodeToString(code.getBytes());
            return Result.success(Base64Code);
        } else {
            return Result.error("授权失败！");
        }
    }

/*
    @GetMapping("/login/oauth/authorize")
    public Result authorizeBangumi() {
        return Result.success(AUTHORIZATION_SUCCESS);
    }
*/

}
