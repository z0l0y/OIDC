package com.hust.utils;

import com.hust.dto.*;
import com.hust.po.*;
import com.hust.vo.*;
import org.ini4j.Profile;
import org.springframework.web.bind.annotation.RequestParam;

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

    public static AnimePO toAnimePO(AnimeDTO animeDTO) {
        // 过个滤先，避免到时候字段不全发生NPE
        if (animeDTO.getAvatar() == null) {
            animeDTO.setAvatar("");
        }
        if (animeDTO.getDirector() == null) {
            animeDTO.setDirector("");
        }
        if (animeDTO.getEpisodes() == null) {
            animeDTO.setEpisodes(1);
        }
        if (animeDTO.getIntroduction() == null) {
            animeDTO.setIntroduction("");
        }
        if (animeDTO.getName() == null) {
            animeDTO.setName("");
        }
        AnimePO animePO = new AnimePO();
        animePO.setName(animeDTO.getName());
        animePO.setEpisodes(animeDTO.getEpisodes());
        animePO.setDirector(animeDTO.getDirector());
        animePO.setAvatar(animeDTO.getAvatar());
        animePO.setIntroduction(animeDTO.getIntroduction());
        return animePO;
    }

    public static CollectionPO toCollectionPO(CollectDTO collectDTO) {
        CollectionPO collectionPO = new CollectionPO();
        collectionPO.setUsername(collectDTO.getUsername());
        collectionPO.setAnimeName(collectDTO.getAnimeName());
        collectionPO.setType(collectDTO.getType());
        return collectionPO;
    }

    public static RatingPO toRatingPO(CollectDTO collectDTO) {
        RatingPO ratingPO = new RatingPO();
        ratingPO.setUsername(collectDTO.getUsername());
        ratingPO.setRatingValue(collectDTO.getRatingValue());
        ratingPO.setAnimeName(collectDTO.getAnimeName());
        ratingPO.setCommentary(collectDTO.getCommentary());
        return ratingPO;
    }

    public static CollectionVO toCollectionVO(CollectionPO collectionPO) {
        CollectionVO collectionVO = new CollectionVO();
        collectionVO.setAnimeName(collectionPO.getAnimeName());
        collectionVO.setType(collectionPO.getType());
        return collectionVO;
    }

    public static FriendShipVO toFriendShipVO(FriendShipPO friendShipPO) {
        FriendShipVO friendShipVO = new FriendShipVO();
        friendShipVO.setUser1(friendShipPO.getUser1());
        return friendShipVO;
    }

    public static RatingVO toRatingVO(RatingPO ratingPO) {
        RatingVO ratingVO = new RatingVO();
        ratingVO.setAnimeName(ratingPO.getAnimeName());
        ratingVO.setRatingValue(ratingPO.getRatingValue());
        ratingVO.setCommentary(ratingPO.getCommentary());
        ratingVO.setGmtModified(ratingPO.getGmtModified());
        return ratingVO;
    }

    public static AuthorizeDTO toAuthorizeDTO(String responseType, String clientId, String redirectUrl, String scope, String state) {
        AuthorizeDTO authorizeDTO = new AuthorizeDTO();
        authorizeDTO.setResponse_type(responseType);
        authorizeDTO.setClient_id(clientId);
        authorizeDTO.setRedirect_url(redirectUrl);
        authorizeDTO.setScope(scope);
        authorizeDTO.setState(state);
        return authorizeDTO;
    }

    public static ProfileVO toProfileVO(ResourceInfoVO resourceInfoVO) {
        ProfileVO profileVO = new ProfileVO();
        profileVO.setUsername(resourceInfoVO.getUsername());
        profileVO.setNickname(resourceInfoVO.getNickname());
        profileVO.setAvatar(resourceInfoVO.getAvatar());
        return profileVO;
    }

    public static ProfileVO toProfileVO(ResourcePO resourcePO) {
        ProfileVO profileVO = new ProfileVO();
        profileVO.setUsername(resourcePO.getUsername());
        profileVO.setNickname(resourcePO.getNickname());
        profileVO.setAvatar(resourcePO.getAvatar());
        return profileVO;
    }
}
