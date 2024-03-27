package com.hust.service;

import com.hust.dto.FriendDTO;
import com.hust.utils.Result;

public interface FriendShipService {
    Result applyFriend(FriendDTO friendDTO);

    Result seePendingMsg(FriendDTO friendDTO);

    Result agreeFriend(FriendDTO friendDTO);

    Result disagreeFriend(FriendDTO friendDTO);

    Result notifyUser1(FriendDTO friendDTO);
}
