package com.hust.controller;

import com.hust.dto.FriendDTO;
import com.hust.service.FriendShipService;
import com.hust.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result applyFriend(@RequestBody FriendDTO friendDTO) {
        return friendShipService.applyFriend(friendDTO);
    }

    /**
     * 别人申请之后，你可以在你的信息里面收到，显示有谁想成为你的好友
     *
     * @return
     */
    @PostMapping("/pending")
    public Result seePendingMsg(@RequestBody FriendDTO friendDTO) {
        return friendShipService.seePendingMsg(friendDTO);
    }

    /**
     * 同意之后status会变为1，拒绝后status还是0
     */
    @PostMapping("/agree")
    public Result agreeFriend(@RequestBody FriendDTO friendDTO) {
        return friendShipService.agreeFriend(friendDTO);
    }

    @PostMapping("/disagree")
    public Result disagreeFriend(@RequestBody FriendDTO friendDTO) {
        return friendShipService.disagreeFriend(friendDTO);
    }

    @PostMapping("/notify")
    public Result notifyUser1(@RequestBody FriendDTO friendDTO){
        return friendShipService.notifyUser1(friendDTO);
    }
}
