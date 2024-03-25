package com.hust.utils;

import com.hust.dto.UserDTO;
import com.hust.po.UserPO;
import com.hust.vo.UserVO;

public class Conversion {
    /**
     * 将用户传输对象转换为用户持久化对象.
     *
     * @param userDTO 用户传输对象，包含用户信息
     * @return 转换后的用户持久化对象UserPO
     */
    public static UserPO toUserPO(UserDTO userDTO) {
        UserPO userPO = new UserPO();
        if (userDTO.getUsername() == null) {
            userDTO.setUsername("");
        }
        if (userDTO.getPassword() == null) {
            userDTO.setPassword("");
        }
        if (userDTO.getEmail() == null) {
            userDTO.setEmail("");
        }
        if (userDTO.getBio() == null) {
            userDTO.setBio("");
        }
        userPO.setUsername(userDTO.getUsername());
        userPO.setPassword(userDTO.getPassword());
        userPO.setEmail(userDTO.getEmail());
        userPO.setNickname(userDTO.getNickname());
        userPO.setBio(userDTO.getBio());
        return userPO;
    }

    public static UserVO toUserVO(UserPO userPO) {
        UserVO userVO = new UserVO();
        userVO.setUsername(userPO.getUsername());
        userVO.setNickname(userPO.getNickname());
        userVO.setAvatar(userPO.getAvatar());
        userVO.setBio(userPO.getBio());
        return userVO;
    }
}
