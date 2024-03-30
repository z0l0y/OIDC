package com.hust.controller;

import com.hust.dto.*;
import com.hust.po.ResourcePO;
import com.hust.pojo.IDToken;
import com.hust.pojo.Token;
import com.hust.service.AuthorizationService;
import com.hust.utils.*;
import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import static com.hust.utils.CodeUtils.parseCode;
import static com.hust.utils.Conversion.toAuthorizeDTO;
import static com.hust.utils.ExamineTokenExpire.examineToken;
import static com.hust.utils.IDTokenUtils.createIDToken;
import static com.hust.utils.IDTokenUtils.createJWEToken;
import static com.hust.utils.JwtUtils.parseJWT;
import static com.hust.utils.RefreshTokenUtils.parseRefreshToken;
import static com.hust.utils.StorageUtils.storageToken;

@RestController
public class AuthorizationController {
    @Autowired
    private AuthorizationService authorizationService;

    private static final String AUTHORIZATION_SUCCESS = "申请授权第三方成功！";

    /**
     * 0，首先你这个第三方要在我授权服务器上面注册你的相关信息，顺便放回给你一个唯一的Client_id
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
     * 已规范！
     * 1，然后，用户点击用其他平台的账号登录才能调用这样一个接口，用于在我们的授权服务器里面存储好state，便于防范CSRF攻击
     * 注意前端应该先调用我们ClientController的方法，产生一个state后才能调用这个方法，将state加到URL里面
     * 类似于用GitHub，刚进去的时候的页面上面的URL
     * 发这个请求的目的是将Client生成的state存储起来，存到我们的授权服务器，authorization_state里面
     *
     * @return 进入页面是否成功
     */
    @GetMapping("/authorize")
    public Object authorizeMyApp(@RequestParam(value = "response_type", defaultValue = "") String responseType,
                                 @RequestParam(value = "client_id", defaultValue = "") String clientId,
                                 @RequestParam(value = "redirect_url", defaultValue = "") String redirectUrl,
                                 @RequestParam(value = "scope", defaultValue = "") String scope,
                                 @RequestParam(value = "state", defaultValue = "") String state,
                                 @RequestHeader("Authorization") String authorizationHeader) {
        if ("".equals(clientId) || "".equals(redirectUrl) || "".equals(scope)) {
            return Result.error("不要省去必选的参数！");
        }
        if (!scope.contains("openid")) {
            return Result.error("OIDC的请求必须包含值为“openid”的scope的参数");
        }
        if (!"code".equals(responseType)) {
            return Result.error("目前只支持授权码方式！");
        }
/*        // 调用 /verify/state 接口
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/verify/state";
        StateDTO stateDTO = new StateDTO();
        stateDTO.setState(state);
        Result verifyStateResult = restTemplate.postForObject(url, stateDTO, Result.class);
        if (verifyStateResult.getCode() == 0) {
            return verifyStateResult;
        }*/
        AuthorizeDTO authorizeDTO = toAuthorizeDTO(responseType, clientId, redirectUrl, scope, state);
        // 插入从这个请求中获取到的state参数，便于我们后期进行比对
        Result authorize = authorizationService.authorize(authorizeDTO);
        if (authorize.getCode() == 1) {
/*
            // 构建请求参数
            VerifyDTO verifyDTO = new VerifyDTO();
            // 设置 verifyDTO 的参数

            // 发起 POST 请求重定向到 /authenticate 接口
            RestTemplate restTemplate = new RestTemplate();
            String authenticateUrl = "http://localhost:8080/authenticate";  // 替换为实际的 /authenticate 接口的 URL
            Result verifyPass = restTemplate.postForObject(authenticateUrl, verifyDTO, Result.class);
*/
            Claims claims;
            try {
                // 提取JWT令牌的内容
                String token = authorizationHeader.substring(7); // 去除"Bearer "前缀
                claims = parseJWT(token);
            } catch (RuntimeException e) {
                return Result.error("请您重新登录bangumoe,登录的token已过期");
            }
            String username = (String) claims.get("username");
            String password = (String) claims.get("password");
            HashMap<Object, Object> userInfo = authorizationService.getUserInfo(username, password, scope);

            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8080/authenticate";
            VerifyDTO verifyDTO = new VerifyDTO();
            verifyDTO.setUsername(username);
            verifyDTO.setPassword(password);
            Result verifyStateResult = restTemplate.postForObject(url, verifyDTO, Result.class);
            if (verifyStateResult.getCode() == 0) {
                return verifyStateResult;
            }
            HashMap<Object, Object> map = new HashMap<>();
            map.put("userInfo", userInfo);
            map.put("code", verifyStateResult.getData());
            map.put("state", state);

            return Result.success(map);
        } else if (authorize.getData() != null) {
            return authorize;
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
     * 参数一致！
     * 3，在我们的授权服务器发放code之后，就可以完全在我们的后端来处理逻辑了，注意token的时效性，首先要验证然后才能进行下面的逻辑
     *
     * @param tokenDTO 包含了我们刚才获取到的code，还有第三方的client_id和client_secret，准备获取access_token去资源服务器获取我们的数据然后给第三方了！
     * @return 返回access token和refresh token，同样注意时效性
     */
    @PostMapping("/token")
    public Result getAccessToken(@RequestBody TokenDTO tokenDTO) throws JOSEException {
        String code = "";
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(tokenDTO.getCode());
            code = new String(decodedBytes);
            System.out.println(code);
            parseCode(code);
        } catch (RuntimeException e) {
            return Result.error("code被恶意修改或已失效，请您重新授权！");
        }
        Result result = new Result();
        try {
            result = examineToken(code);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Result.error("您的code已失效，请您重新授权！");
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
                String iss = "http://localhost:8080/authorize";
                String sub = UUID.randomUUID().toString().replace("-", "");
                String aud = tokenDTO.getClient_id();
                Date iat = new Date(System.currentTimeMillis());
                Date exp = new Date(System.currentTimeMillis() + 60 * 60 * 1000L);
                String nonce = UUID.randomUUID().toString().replace("-", "");
                String picture = ((ResourcePO) verify.getData()).getAvatar();
                String nickname = ((ResourcePO) verify.getData()).getNickname();
                String name = ((ResourcePO) verify.getData()).getUsername();
                String email = ((ResourcePO) verify.getData()).getEmail();
                String idToken = createJWEToken(iss, sub, aud, exp, iat, nonce, picture, nickname, name, email);
                token.put("id_token", idToken);
                token.put("token_type", "Bearer");
                token.put("expires_in", 600000);
                token.put("refresh_token", base64RefreshToken);
                Token storageToken = storageToken(accessToken, refreshToken, code);
                authorizationService.storageToken(storageToken);
                return Result.success(token);
            } else {
                return Result.error("请检查您的客户端ID和客户端密钥是否正确！");
            }
        } else {
            return Result.error("code非法，请您重新授权！");
        }
    }

/*
    @GetMapping("/oauth2")
    public void handleOAuth2Response(@RequestParam("code") String code,
                                     @RequestParam("state") String state,
                                     HttpServletResponse response) throws IOException {
        // 处理授权码和状态的逻辑
        // ...

        String redirectUri = "https://client.example.com/redirect"; // 客户端提供的redirect_uri
        String encodedCode = URLEncoder.encode(code, "UTF-8");
        String encodedState = URLEncoder.encode(state, "UTF-8");

        // 构建重定向URL，包含授权码和状态
        String redirectLocation = redirectUri + "?code=" + encodedCode + "&state=" + encodedState;

        // 重定向到客户端提供的redirect_uri
        response.setStatus(HttpStatus.FOUND.value());
        response.setHeader("Location", redirectLocation);
    }*/

/*    @GetMapping("/oauth2")
    public void handleOAuth2Response(HttpServletResponse response) throws IOException {
        // 获取授权码和state
        String code = "SplxlOBeZQQYbYS6WxSbIA";
        String state = "xyz";

        // 构建重定向URL
        String redirectUri = "https://client.example.com/oauth2";
        String encodedCode = URLEncoder.encode(code, "UTF-8");
        String encodedState = URLEncoder.encode(state, "UTF-8");
        String redirectLocation = redirectUri + "?code=" + encodedCode + "&state=" + encodedState;

        // 设置响应状态码和Location头部信息
        response.setStatus(HttpStatus.FOUND.value());
        response.setHeader("Location", redirectLocation);
    }*/
}


