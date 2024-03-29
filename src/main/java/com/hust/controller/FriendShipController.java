package com.hust.controller;

import com.hust.dto.FriendDTO;
import com.hust.dto.FriendListDTO;
import com.hust.service.FriendShipService;
import com.hust.utils.Result;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

import static com.hust.utils.JwtUtils.parseJWT;

@RestController
@RequestMapping("/friend")
public class FriendShipController {
    @Autowired
    private FriendShipService friendShipService;

    /**
     * 首先要申请好友才行，这是第一步
     *
     * @return
     */
    @PostMapping("/apply")
    public Result applyFriend(@RequestBody FriendDTO friendDTO, @RequestHeader("Authorization") String authorizationHeader) {
        Claims claims;
        try {
            // 提取JWT令牌的内容
            String token = authorizationHeader.substring(7); // 去除"Bearer "前缀
            claims = parseJWT(token);
        } catch (RuntimeException e) {
            return Result.error("请您重新登录bangumoe,登录的token已过期");
        }
        friendDTO.setUser1((String) claims.get("username"));
        return friendShipService.applyFriend(friendDTO);
    }

    /**
     * 别人申请之后，你可以在你的信息里面收到，显示有谁想成为你的好友
     *
     * @return
     */
    @GetMapping("/pending")
    public Result seePendingMsg(@RequestHeader("Authorization") String authorizationHeader) {
        Claims claims;
        try {
            // 提取JWT令牌的内容
            String token = authorizationHeader.substring(7); // 去除"Bearer "前缀
            claims = parseJWT(token);
        } catch (RuntimeException e) {
            return Result.error("请您重新登录bangumoe,登录的token已过期");
        }
        FriendDTO friendDTO = new FriendDTO();
        friendDTO.setUser2((String) claims.get("username"));
        return friendShipService.seePendingMsg(friendDTO);
    }

    /**
     * 同意之后status会变为1，拒绝后status还是0
     */
    @PostMapping("/agree")
    public Result agreeFriend(@RequestBody FriendDTO friendDTO, @RequestHeader("Authorization") String authorizationHeader) {
        Claims claims;
        try {
            // 提取JWT令牌的内容
            String token = authorizationHeader.substring(7); // 去除"Bearer "前缀
            claims = parseJWT(token);
        } catch (RuntimeException e) {
            return Result.error("请您重新登录bangumoe,登录的token已过期");
        }
        friendDTO.setUser2((String) claims.get("username"));
        return friendShipService.agreeFriend(friendDTO);
    }

    @PostMapping("/disagree")
    public Result disagreeFriend(@RequestBody FriendDTO friendDTO, @RequestHeader("Authorization") String authorizationHeader) {
        Claims claims;
        try {
            // 提取JWT令牌的内容
            String token = authorizationHeader.substring(7); // 去除"Bearer "前缀
            claims = parseJWT(token);
        } catch (RuntimeException e) {
            return Result.error("请您重新登录bangumoe,登录的token已过期");
        }
        friendDTO.setUser2((String) claims.get("username"));
        return friendShipService.disagreeFriend(friendDTO);
    }

    @GetMapping("/notify")
    public Result notifyUser1(@RequestHeader("Authorization") String authorizationHeader) {
        Claims claims;
        try {
            // 提取JWT令牌的内容
            String token = authorizationHeader.substring(7); // 去除"Bearer "前缀
            claims = parseJWT(token);
        } catch (RuntimeException e) {
            return Result.error("请您重新登录bangumoe,登录的token已过期");
        }
        String username = (String) claims.get("username");

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/friend/all";

        HttpHeaders headers = new HttpHeaders();
        // 这一块为了演示也只能暂时采用硬编码的形式了（因为浏览器是可以存token的，前端也更加的好处理，但是后端这一块不太灵活）
        headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6InJvb3RodXN0IiwidXNlcm5hbWUiOiJyb290IiwiZXhwIjoxNzE0MzIwNTU4fQ.QTmJTn5XePXugfZjGHlB3TEqHh-wegZDXpkvD7TRa_A");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<FriendListDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, FriendListDTO.class);
        FriendListDTO result = response.getBody();
        List<String> data = result.getFriendList();
        FriendListDTO friendListDTO = new FriendListDTO();
        friendListDTO.setFriendList(data);
        return friendShipService.notifyUser2(username, friendListDTO);
    }

    @GetMapping("/all")
    public FriendListDTO showMyAllFriends(@RequestHeader("Authorization") String authorizationHeader) {
        Claims claims;
        FriendListDTO friendListDTO = new FriendListDTO();
        try {
            // 提取JWT令牌的内容
            String token = authorizationHeader.substring(7); // 去除"Bearer "前缀
            claims = parseJWT(token);
        } catch (RuntimeException e) {
            List<String> error = new ArrayList<>();
            error.add("请您重新登录bangumoe,登录的token已过期");
            friendListDTO.setFriendList(error);
            return friendListDTO;
        }
        String username = (String) claims.get("username");
        List<String> result = friendShipService.showMyAllFriends(username);
        friendListDTO.setFriendList(result);
        return friendListDTO;
    }
}
