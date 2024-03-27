package com.hust.utils;

import com.hust.dto.*;
import com.hust.po.AppPO;
import com.hust.po.ResourcePO;
import com.hust.po.UserPO;
import com.hust.vo.ResourceInfoVO;
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
        if (userDTO.getNickname() == null) {
            userDTO.setNickname("");
        }
        if (userDTO.getAvatar() == null) {
            userDTO.setAvatar("");
        }
        userPO.setUsername(userDTO.getUsername());
        userPO.setPassword(userDTO.getPassword());
        userPO.setEmail(userDTO.getEmail());
        userPO.setNickname(userDTO.getNickname());
        userPO.setBio(userDTO.getBio());
        userPO.setAvatar(userDTO.getAvatar());
        return userPO;
    }

    public static AppPO toAppPO(AppDTO appDTO) {
        AppPO appPO = new AppPO();
        appPO.setName(appDTO.getName());
        appPO.setClientSecret(appDTO.getClientSecret());
        appPO.setRedirectUrl(appDTO.getRedirectUrl());
        return appPO;
    }

    public static AppPO toAppPO(AuthorizeDTO authorizeDTO) {
        AppPO appPO = new AppPO();
        appPO.setRedirectUrl(authorizeDTO.getRedirect_url());
        appPO.setClientId(authorizeDTO.getClient_id());
        return appPO;
    }

    public static UserVO toUserVO(UserPO userPO) {
        UserVO userVO = new UserVO();
        userVO.setUsername(userPO.getUsername());
        userVO.setNickname(userPO.getNickname());
        userVO.setAvatar(userPO.getAvatar());
        userVO.setBio(userPO.getBio());
        return userVO;
    }

    public static ResourcePO toResourcePO(VerifyDTO verifyDTO) {
        ResourcePO resourcePO = new ResourcePO();
        resourcePO.setUsername(verifyDTO.getUsername());
        resourcePO.setPassword(verifyDTO.getPassword());
        return resourcePO;
    }

    public static AppPO toAppPO(TokenDTO tokenDTO) {
        AppPO appPO = new AppPO();
        appPO.setClientId(tokenDTO.getClient_id());
        appPO.setClientSecret(tokenDTO.getClient_secret());
        appPO.setRedirectUrl(tokenDTO.getRedirect_uri());
        return appPO;
    }

    public static ResourceInfoVO toResourceInfoVO(ResourcePO resourcePO) {
        ResourceInfoVO resourceInfoVO = new ResourceInfoVO();
        resourceInfoVO.setUsername(resourcePO.getUsername());
        resourceInfoVO.setNickname(resourcePO.getNickname());
        resourceInfoVO.setEmail(resourcePO.getEmail());
        resourceInfoVO.setAvatar(resourcePO.getAvatar());
        resourceInfoVO.setBio(resourcePO.getBio());
        return resourceInfoVO;
    }
}
