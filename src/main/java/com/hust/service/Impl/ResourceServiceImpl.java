package com.hust.service.Impl;

import com.hust.dto.AccessTokenDTO;
import com.hust.dto.VerifyDTO;
import com.hust.mapper.ResourceMapper;
import com.hust.po.AppPO;
import com.hust.po.ResourcePO;
import com.hust.pojo.Code;
import com.hust.service.ResourceService;
import com.hust.utils.AccessTokenUtils;
import com.hust.utils.ExamineTokenExpire;
import com.hust.utils.Result;
import com.hust.vo.ProfileVO;
import com.hust.vo.ResourceInfoVO;
import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

import static com.hust.utils.AccessTokenUtils.parseAccessToken;
import static com.hust.utils.CodeUtils.parseCode;
import static com.hust.utils.Conversion.*;
import static com.hust.utils.ExamineTokenExpire.examineToken;
import static com.hust.utils.IDTokenUtils.*;
import static com.hust.utils.RefreshTokenUtils.parseRefreshToken;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public Result verifyIdentity(VerifyDTO verifyDTO) {
        ResourcePO resourcePO = toResourcePO(verifyDTO);
        ResourcePO resourcePO1 = resourceMapper.verifyIdentity(resourcePO);
        if (resourcePO1 != null) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @Override
    public Result storageCode(Code code) {
        resourceMapper.updateCode(code);
        int rowsAffected = resourceMapper.updateCode(code);
        if (rowsAffected > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @Override
    public Result getUserInfo(AccessTokenDTO accessTokenDTO, String idToken) throws ParseException, JOSEException {
        byte[] decodedBytes;
        Claims claims1;
        Claims claims2;
        byte[] decodedRefreshToken;
        String refreshToken;
        ResourceInfoVO resourceInfoVO;
        try {
            decodedBytes = Base64.getDecoder().decode(accessTokenDTO.getAccessToken());
        } catch (RuntimeException e) {
            return Result.error("accessToken被恶意修改或已经失效!");
        }
        String accessToken1 = new String(decodedBytes);
        // 阿里巴巴开发手册里面说过，要对try-catch负责，首先我们不能直接一大块全部try-catch，我们try-catch的颗粒度一定要细（精准try-catch），不能太粗了，其次是我们应该对try-catch部分发生的错误好好说明，对于前端的开发人员更加的友好
        try {
            claims1 = parseAccessToken(accessToken1);
            String token = (String) claims1.get("accessToken");
            ResourcePO userInfo = resourceMapper.getUserInfo(token);
            resourceInfoVO = toResourceInfoVO(userInfo);
        } catch (RuntimeException e) {
            // AccessToken过期，尝试使用RefreshToken获取新的AccessToken
            try {
                decodedRefreshToken = Base64.getDecoder().decode(accessTokenDTO.getRefreshToken());
                refreshToken = new String(decodedRefreshToken);
                claims2 = parseRefreshToken(refreshToken);
            } catch (RuntimeException exception) {
                return Result.error("refreshToken被恶意修改或已经失效!");
            }
            // 使用RefreshToken获取新的AccessToken的逻辑
            try {
                examineToken(refreshToken);
            } catch (RuntimeException runtimeException) {
                return Result.error("RefreshToken已失效，请您重新登录一下吧!");
            }
            ResourcePO result1 = resourceMapper.verifyAccessToken(refreshToken);
            ResourcePO result2 = resourceMapper.verifyRefreshToken(refreshToken);
            if (result1 == null && result2 == null) {
                return Result.error("accessToken和refreshToken已经失效或被恶意修改，请重新获取Token发起请求！");
            }
            String token = (String) claims2.get("refreshToken");
            // 获取到新的AccessToken后，继续下面的逻辑
            Map<String, Object> newClaims = new HashMap<>();
            String uuidAccessToken = UUID.randomUUID().toString().replace("-", "");
            newClaims.put("accessToken", uuidAccessToken);
            // 注意一下，这个是有两个.的JWT令牌，注意下面查询的时候不能用这个
            String token1 = AccessTokenUtils.generateAccessToken(newClaims);
            resourceMapper.updateAccessToken(token1, refreshToken);
            ResourcePO userInfo = resourceMapper.getUserInfo(token1);
            resourceInfoVO = toResourceInfoVO(userInfo);
            /*            ProfileVO profileVO = toProfileVO(resourceInfoVO);*/
        }
        Claims claims;
        try {
            decodedRefreshToken = Base64.getDecoder().decode(accessTokenDTO.getRefreshToken());
            refreshToken = new String(decodedRefreshToken);
            claims2 = parseRefreshToken(refreshToken);
            examineToken(refreshToken);
        } catch (RuntimeException exception) {
            return Result.error("refreshToken被恶意修改或已经失效!");

        }
        try {
            claims = parseIDToken(decryptJWEToken(idToken));
        } catch (RuntimeException runtimeException) {
            return Result.error("请不要恶意修改JWE令牌的信息！");
        }
        try {
            System.out.println(claims.get("iss"));
            if (!"http://localhost:8080/authorize".equals(claims.get("iss"))) {
                return Result.error("iss被修改！");
            }
            if (!"c556723844614ec2a13a270cc8847fc8".equals((String) claims.get("aud"))) {
                return Result.error("aud被修改！");
            }
            examineToken(decryptJWEToken(idToken));
        } catch (RuntimeException exception) {
            String iss = "http://localhost:8080/authorize";
            String sub = UUID.randomUUID().toString().replace("-", "");
            String aud = "c556723844614ec2a13a270cc8847fc8";
            Date iat = new Date(System.currentTimeMillis());
            Date exp = new Date(System.currentTimeMillis() + 60 * 60 * 1000L);
            String nonce = UUID.randomUUID().toString().replace("-", "");
            String picture = resourceInfoVO.getAvatar();
            String nickname = resourceInfoVO.getNickname();
            String name = resourceInfoVO.getUsername();
            String email = resourceInfoVO.getEmail();
            String idTokenRefresh = createJWEToken(iss, sub, aud, exp, iat, nonce, picture, nickname, name, email);
            System.out.println(decryptJWEToken(idToken));
            claims = parseIDToken(decryptJWEToken(idToken));
        }

        HashMap<Object, Object> map = new HashMap<>();
        String scope = resourceMapper.getScope(refreshToken).getScope();
        if (scope.contains("openid")) {
            map.put("openid", claims);
        }
        if (scope.contains("email")) {
            map.put("email", resourceInfoVO.getEmail());
        }
        if (scope.contains("profile")) {
            map.put("profile", toProfileVO(resourceInfoVO));
        }
        return Result.success(map);
    }
}
