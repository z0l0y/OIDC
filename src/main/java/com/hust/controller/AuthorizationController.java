package com.hust.controller;

import com.hust.dto.AppDTO;
import com.hust.dto.AuthorizeDTO;
import com.hust.dto.TokenDTO;
import com.hust.dto.StateDTO;
import com.hust.pojo.IDToken;
import com.hust.pojo.Token;
import com.hust.service.AuthorizationService;
import com.hust.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static com.hust.utils.CodeUtils.parseCode;
import static com.hust.utils.ExamineTokenExpire.examineToken;
import static com.hust.utils.IDTokenUtils.createIDToken;
import static com.hust.utils.RefreshTokenUtils.parseRefreshToken;
import static com.hust.utils.StorageUtils.storageToken;

@RestController
public class AuthorizationController {
    @Autowired
    private AuthorizationService authorizationService;

    private static final String AUTHORIZATION_SUCCESS = "申请授权第三方成功！";

    /**
     * 1，首先你这个第三方要在我授权服务器上面注册你的相关信息，顺便放回给你一个唯一的Client_id
     *
     * @param appDTO
     * @return
     */
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

    /**
     * ，2.2，然后，用户点击用其他平台的账号登录才能调用这样一个接口，用于在我们的授权服务器里面存储好state，便于防范CSRF攻击
     * 注意前端应该先调用我们ClientController的方法，产生一个state后才能调用这个方法，将state加到URL里面
     *
     * @param authorizeDTO 类似于用GitHub，刚进去的时候的页面上面的URL，发这个请求的目的是将Client生成的state存储起来
     * @return 进入页面是否成功
     */
    @PostMapping("/login/oauth/authorize")
    public Result authorizeMyApp(@RequestBody AuthorizeDTO authorizeDTO) {
        // 插入从这个请求中获取到的state参数，便于我们后期进行比对
        Result authorize = authorizationService.authorize(authorizeDTO);
        if (authorize.getCode() == 1) {
            return Result.success("成功与授权服务器建立起连接！");
        } else {
            return Result.error("请不要随意修改URL上的信息，与授权服务器建立连接失败！");
        }
    }



    /*
        @GetMapping("/login/oauth/authorize")
        public Result authorizeBangumi() {
            return Result.success(AUTHORIZATION_SUCCESS);
        }
    */

    /**
     * 5，在我们的授权服务器发放code之后，就可以完全在我们的后端来处理逻辑了，注意token的时效性，首先要验证然后才能进行下面的逻辑
     *
     * @param tokenDTO 包含了我们刚才获取到的code，还有第三方的client_id和client_secret，准备获取access_token去资源服务器获取我们的数据然后给第三方了！
     * @return 返回access token和refresh token，同样注意时效性
     */
    @PostMapping("/get/token")
    public Result getAccessToken(@RequestBody TokenDTO tokenDTO) {
        String code = "";
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(tokenDTO.getCode());
            code = new String(decodedBytes);
            parseCode(code);
        } catch (RuntimeException e) {
            return Result.error("code被恶意修改，请您重新授权！");
        }
        Result result = new Result();
        try {
            result = examineToken(code);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Result.error("您的code已过期，请重新授权！");
        }
        if (result.getCode() == 1) {
            Result verify = authorizationService.verifyClientInfo(tokenDTO);
            if (verify.getCode() == 1) {
                Map<String, Object> claims = new HashMap<>();
                String uuidAccessToken = UUID.randomUUID().toString().replace("-", "");
                String uuidRefreshToken = UUID.randomUUID().toString().replace("-", "");
                claims.put("accessToken", uuidAccessToken);
                claims.put("refreshToken", uuidRefreshToken);
                String accessToken = AccessTokenUtils.generateAccessToken(claims);
                String refreshToken = RefreshTokenUtils.generateRefreshToken(claims);
                String base64AccessToken = Base64.getEncoder().encodeToString(accessToken.getBytes());
                String base64RefreshToken = Base64.getEncoder().encodeToString(refreshToken.getBytes());
                Map<String, Object> token = new HashMap<>();
                token.put("access_token", base64AccessToken);
                String iss = "https://auth.bangumi.com";
                String sub = UUID.randomUUID().toString().replace("-", "");
                String aud = tokenDTO.getClient_id();
                Date iat = new Date(System.currentTimeMillis());
                Date exp = new Date(System.currentTimeMillis() + 60 * 60 * 1000L);
                String nonce = UUID.randomUUID().toString().replace("-", "");
                String idToken = createIDToken(iss, sub, aud, exp, iat, nonce);
                token.put("id_token", idToken);
                token.put("token_type", "Bearer");
                token.put("expires_in", 600000);
                token.put("refresh_token", base64RefreshToken);
                Token storageToken = storageToken(uuidAccessToken, uuidRefreshToken, tokenDTO.getCode());
                authorizationService.storageToken(storageToken);
                return Result.success(token);
            } else {
                return Result.error("请检查您的客户端ID和客户端密钥是否正确！");
            }
        } else {
            return Result.error("code非法，请重新授权！");
        }
    }
}
