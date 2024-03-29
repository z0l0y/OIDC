package com.hust.controller;


import com.hust.dto.StateDTO;
import com.hust.dto.TokenDTO;
import com.hust.service.ClientService;
import com.hust.utils.JwtUtils;
import com.hust.utils.Result;
import com.hust.utils.StateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class ClientController {
    @Autowired
    private ClientService clientService;


    /**
     * 客户端将用户重定向到授权服务器，获取到客户端生成的state参数作为自己URL的一部分
     * 客户端产生state，将state存储到客户端服务器的client_state里面
     */
    @GetMapping("/get/state")
    public Result getState() {
        Map<String, Object> claims = new HashMap<>();
        String uuidState = UUID.randomUUID().toString().replace("-", "");
        claims.put("code", uuidState);
        String state = StateUtils.generateState(claims);
        String base64State = Base64.getEncoder().encodeToString(state.getBytes());
/*        // 解码
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes);*/
        // 不不不我们不用考虑这么多因为Base64一般也不会重名的，我们只用看要有没有就行了
        clientService.storeState(base64State);
        return Result.success(base64State);
    }

    /**
     * 验证 state 值，确保请求的完整性和安全性，然后再进一步验证用户的身份，通常通过用户登录、验证凭据等方式来确认用户的身份
     * 这里我们只能在这里来进行state的验证，毕竟只有我们的Client数据库里面存储的才是原始的state
     * 这个接口的作用就是过滤一下非法的state，防止CSRF攻击，没有对数据库的增删改逻辑
     *
     * @param stateDTO 里面就只有一个state
     * @return state是否合法
     */
    @PostMapping("/verify/state")
    public Result verifyState(@RequestBody StateDTO stateDTO) {
        // 单纯的用JWT返回的格式有一点奇怪，有.，很明显就是JWT，为了掩盖一下，我采用了Base64来先编码一下
        Result verifyState = clientService.verifyState(stateDTO);
        if (verifyState.getCode() == 1) {
            return Result.success("state验证成功，现在可以可以到获取token的阶段了！");
        } else {
            return verifyState;
        }
    }

    @GetMapping("/auth/bangumi/callback")
    public Result callBack(@RequestParam("code") String code, @RequestParam("state") String state) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/verify/state";
        StateDTO stateDTO = new StateDTO();
        stateDTO.setState(state);
        Result verifyStateResult = restTemplate.postForObject(url, stateDTO, Result.class);
        return verifyStateResult;
    }

    private Map<String, Object> map = new HashMap<>();

    private int flag = 0;

    @GetMapping("/well-known/openid-configuration")
    public Map<String, Object> showOpenidConfiguration() {
        if (flag == 0) {
            // 如果缓存中不存在数据，则进行数据的生成和缓存
            map.put("issuer", new String[]{"http://localhost:8080/authorize"});
            map.put("authorization_endpoint", new String[]{"http://localhost:8080/authenticate"});
            map.put("token_endpoint", new String[]{"http://localhost:8080/token"});
            map.put("userinfo_endpoint", new String[]{"http://localhost:8080/userinfo"});
            map.put("response_types_supported", new String[]{"code"});
            map.put("id_token_signing_alg_values_supported", new String[]{"HS256"});
            map.put("scopes_supported", new String[]{"openid", "email", "profile"});
            map.put("claims_supported", new String[]{"sub", "username", "email", "nickname", "avatar", "bio"});
            map.put("grant_types_supported", new String[]{"authorization_code"});
            flag++;
        }
        return map;
    }
}

