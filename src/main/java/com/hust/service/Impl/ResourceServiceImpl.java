package com.hust.service.Impl;

import com.hust.dto.AccessTokenDTO;
import com.hust.dto.VerifyDTO;
import com.hust.mapper.ResourceMapper;
import com.hust.po.AppPO;
import com.hust.po.ResourcePO;
import com.hust.pojo.Code;
import com.hust.service.ResourceService;
import com.hust.utils.AccessTokenUtils;
import com.hust.utils.Result;
import com.hust.vo.ResourceInfoVO;
import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.hust.utils.AccessTokenUtils.parseAccessToken;
import static com.hust.utils.CodeUtils.parseCode;
import static com.hust.utils.Conversion.*;
import static com.hust.utils.ExamineTokenExpire.examineToken;
import static com.hust.utils.IDTokenUtils.decryptJWEToken;
import static com.hust.utils.IDTokenUtils.parseIDToken;
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
        try {
            decodedBytes = Base64.getDecoder().decode(accessTokenDTO.getAccessToken());
        } catch (RuntimeException e) {
            return Result.error("请您不要恶意修改accessToken的信息!");
        }
        String accessToken1 = new String(decodedBytes);
        // 阿里巴巴开发手册里面说过，要对try-catch负责，首先我们不能直接一大块全部try-catch，我们try-catch的颗粒度一定要细（精准try-catch），不能太粗了，其次是我们应该对try-catch部分发生的错误好好说明，对于前端的开发人员更加的友好
        try {
            claims1 = parseAccessToken(accessToken1);
            String token = (String) claims1.get("accessToken");
            ResourcePO userInfo = resourceMapper.getUserInfo(token);
            ResourceInfoVO resourceInfoVO = toResourceInfoVO(userInfo);
            return Result.success(resourceInfoVO);
        } catch (RuntimeException e) {
            // AccessToken过期，尝试使用RefreshToken获取新的AccessToken
            try {
                decodedRefreshToken = Base64.getDecoder().decode(accessTokenDTO.getRefreshToken());
                refreshToken = new String(decodedRefreshToken);
                claims2 = parseRefreshToken(refreshToken);
            } catch (RuntimeException exception) {
                return Result.error("请您不要恶意修改refreshToken的信息!");
            }
            // 使用RefreshToken获取新的AccessToken的逻辑
            try {
                examineToken(refreshToken);
            } catch (RuntimeException runtimeException) {
                return Result.error("RefreshToken过期了，请您重新登录一下吧!");
            }
            ResourcePO result = resourceMapper.verifyToken(accessToken1, refreshToken);
            if (result == null) {
                return Result.error("accessToken和refreshToken已经过时，请换新的Token发起请求！");
            }
            String token = (String) claims2.get("refreshToken");
            // 获取到新的AccessToken后，继续下面的逻辑
            Map<String, Object> newClaims = new HashMap<>();
            String uuidAccessToken = UUID.randomUUID().toString().replace("-", "");
            newClaims.put("accessToken", uuidAccessToken);
            // 注意一下，这个是有两个.的JWT令牌，注意下面查询的时候不能用这个
            AccessTokenUtils.generateAccessToken(newClaims);
            resourceMapper.updateAccessToken(uuidAccessToken, token);
            ResourcePO userInfo = resourceMapper.getUserInfo(uuidAccessToken);
            ResourceInfoVO resourceInfoVO = toResourceInfoVO(userInfo);
            Claims claims = parseIDToken(decryptJWEToken(idToken));
            HashMap<Object, Object> map = new HashMap<>();
            String scope = new String();
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
}
