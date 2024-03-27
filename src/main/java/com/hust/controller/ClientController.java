package com.hust.controller;


import com.hust.service.ClientService;
import com.hust.utils.JwtUtils;
import com.hust.utils.Result;
import com.hust.utils.StateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping("/get/state")
    public Result getState() {
        Map<String, Object> claims = new HashMap<>();
        String uuidState = UUID.randomUUID().toString().replace("-", "");
        claims.put("code", uuidState);
        String state = StateUtils.generateState(claims);
        String Base64State = Base64.getEncoder().encodeToString(state.getBytes());
/*        // 解码
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes);*/
        // 不不不我们不用考虑这么多因为Base64一般也不会重名的，我们只用看要有没有就行了
        clientService.storeState(Base64State);
        return Result.success(Base64State);
    }
}
