package com.hust.service;

import com.hust.dto.FriendDTO;
import com.hust.dto.FriendListDTO;
import com.hust.utils.Result;

import java.util.List;

public interface FriendShipService {
    Result applyFriend(FriendDTO friendDTO);

    Result seePendingMsg(FriendDTO friendDTO);

    Result agreeFriend(FriendDTO friendDTO);

    Result disagreeFriend(FriendDTO friendDTO);

    Result notifyUser2(String username, FriendListDTO friendListDTO);

    List<String> showMyAllFriends(String username);
}
