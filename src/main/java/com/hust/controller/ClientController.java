package com.hust.controller;


import com.hust.dto.StateDTO;
import com.hust.dto.TokenDTO;
import com.hust.service.ClientService;
import com.hust.utils.JwtUtils;
import com.hust.utils.Result;
import com.hust.utils.StateUtils;
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
public class ClientController {
    @Autowired
    private ClientService clientService;


    /**
     * 2.1，客户端将用户重定向到授权服务器，获取到客户端生成的state参数作为自己URL的一部分
     *
     * @return
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
     * 3，验证 state 值，确保请求的完整性和安全性，然后再进一步验证用户的身份，通常通过用户登录、验证凭据等方式来确认用户的身份
     * 这里我们只能在这里来进行state的验证，毕竟只有我们的Client数据库里面存储的才是原始的state
     * 这个接口的作用就是过滤一下非法的state，防止CSRF攻击，没有对数据库的增删改逻辑
     *
     * @param stateDTO 里面就只有一个state
     * @return state是否合法
     */
    @PostMapping("/verify/state")
    public Result verifyState(@RequestBody StateDTO stateDTO) {
        Result verifyState = clientService.verifyState(stateDTO);
        if (verifyState.getCode() == 1) {
            return Result.success("state验证成功，现在可以跳转到用户身份验证的阶段了！");
        } else {
            return Result.error("state失效，请重新操作！");
        }
    }

}
