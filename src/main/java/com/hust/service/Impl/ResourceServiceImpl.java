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
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.hust.utils.AccessTokenUtils.parseAccessToken;
import static com.hust.utils.CodeUtils.parseCode;
import static com.hust.utils.Conversion.*;
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
    public Result getUserInfo(AccessTokenDTO accessTokenDTO) {
        byte[] decodedBytes = new byte[0];
        try {
            decodedBytes = Base64.getDecoder().decode(accessTokenDTO.getAccessToken());
            String accessToken = new String(decodedBytes);
            Claims claims = parseAccessToken(accessToken);
            String token = (String) claims.get("accessToken");
            ResourcePO userInfo = resourceMapper.getUserInfo(token);
            ResourceInfoVO resourceInfoVO = toResourceInfoVO(userInfo);
            return Result.success(resourceInfoVO);
        } catch (RuntimeException e) {
            // AccessToken过期，尝试使用RefreshToken获取新的AccessToken
            try {
                byte[] decodedRefreshToken = Base64.getDecoder().decode(accessTokenDTO.getRefreshToken());
                // 使用RefreshToken获取新的AccessToken的逻辑
                String refreshToken = new String(decodedRefreshToken);
                Claims claims = parseRefreshToken(refreshToken);
                String token = (String) claims.get("refreshToken");
                // 获取到新的AccessToken后，继续下面的逻辑
                Map<String, Object> newClaims = new HashMap<>();
                String uuidAccessToken = UUID.randomUUID().toString().replace("-", "");
                newClaims.put("accessToken", uuidAccessToken);
                // 注意一下，这个是有两个.的JWT令牌，注意下面查询的时候不能用这个
                String accessToken = AccessTokenUtils.generateAccessToken(newClaims);
                resourceMapper.updateAccessToken(uuidAccessToken, token);
                ResourcePO userInfo = resourceMapper.getUserInfo(uuidAccessToken);
                ResourceInfoVO resourceInfoVO = toResourceInfoVO(userInfo);
                return Result.success(resourceInfoVO);
            } catch (RuntimeException refreshException) {
                // RefreshToken过期，提示用户重新登录
                return Result.error("太久没有登录啦。请重新登录一下吧！");
            }
        }
    }
}
