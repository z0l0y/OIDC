package com.hust.controller;

import com.hust.dto.AccessTokenDTO;
import com.hust.dto.VerifyDTO;
import com.hust.pojo.Code;
import com.hust.pojo.Token;
import com.hust.service.ResourceService;
import com.hust.utils.CodeUtils;
import com.hust.utils.Result;
import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.hust.utils.IDTokenUtils.parseIDToken;
import static com.hust.utils.StorageUtils.storageCode;

@RestController
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    /**
     * 2，接下来这个接口是用来在GitHub（类似）页面跳转后的那个授权页面，就是那个有密码的，如果授权成功那么就会发放code
     * 注意这里我们把code存在了resource_info里面，便于我们写后面的取数据的逻辑
     * 重定向在后端不是很好实现，因为要传参数，没有JS灵活，所以目前我考虑过后就不在后端实现了
     *
     * @param verifyDTO 存储的是用户的基本信息，比如username和password，由我们信任的资源服务器来接受我们的信息，这一块不在第三方进行
     */
    @PostMapping("/authenticate")
    public Result verifyUserIdentity(@RequestBody VerifyDTO verifyDTO, HttpServletResponse response) throws IOException {
        Result verifyPass = resourceService.verifyIdentity(verifyDTO);
        if (verifyPass.getCode() == 1) {
            // 登录成功，生成令牌，下发令牌
            Map<String, Object> claims = new HashMap<>();
            String uuidCode = UUID.randomUUID().toString().replace("-", "");
            claims.put("code", uuidCode);
            String code = CodeUtils.generateCode(claims);
            String base64Code = Base64.getEncoder().encodeToString(code.getBytes());
            Code storageCode = storageCode(verifyDTO, code);
            resourceService.storageCode(storageCode);
            return Result.success(base64Code);
        } else {
            return Result.error("用户信息验证失败，授权失败！");
        }
/*            // 构建重定向URL，注意一下，重定向的过程中，前端页面不一定要显示出来
            String redirectUri = "http://localhost:8080/token";
            String encodedCode = URLEncoder.encode(base64Code, StandardCharsets.UTF_8);
            String redirectLocation = redirectUri + "?code=" + encodedCode;

            // 设置Content-Type头部信息
            response.setContentType("application/json");

            // 进行重定向
            response.sendRedirect(redirectLocation);
        } else {
            // 处理验证失败情况
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            // 返回错误信息给客户端
            response.getWriter().write("用户信息验证失败，授权失败！");
        }*/
    }

    /**
     * 4.目前看来就只有state是在第三方得到的，其他的逻辑都在我们信任的资源服务器和授权服务器这边，现在看来确实安全很多
     * 最后一步，就是解析获取到的Base64编码，得到token进行了（除了client_state，其他的都是在我们这边的表，所以第三方也只能拿到state数据，其他的都是拿不到的，所以很安全）
     * <p>
     * 第5步获取到的token（Base64编码格式）
     *
     * @return 用户的信息
     */
/*    @PostMapping("/get/userInfo")
    public Result getUserInfo(@RequestBody AccessTokenDTO accessTokenDTO) {
        return resourceService.getUserInfo(accessTokenDTO);
    }*/
    @GetMapping("/userinfo")
    public Result userinfo(@RequestHeader("Authorization") String authorizationHeader) throws ParseException, JOSEException {
        String accessToken;
        String refreshToken;
        String idToken = "";
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            String[] tokens = authorizationHeader.split(",");
            if (tokens.length == 3) {
                accessToken = tokens[0].replace("Bearer", "").trim();
                refreshToken = tokens[1].replace("RefreshToken", "").trim();
                idToken = tokens[2].replace("IDToken", "").trim();
                accessTokenDTO.setAccessToken(accessToken);
                accessTokenDTO.setRefreshToken(refreshToken);
            }
        }
        return resourceService.getUserInfo(accessTokenDTO, idToken);
    }
}
