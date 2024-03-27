package com.hust.service.Impl;

import com.hust.dto.AppDTO;
import com.hust.dto.AuthorizeDTO;
import com.hust.dto.TokenDTO;
import com.hust.mapper.AuthorizationMapper;
import com.hust.po.AppPO;
import com.hust.po.ResourcePO;
import com.hust.po.UserPO;
import com.hust.pojo.Token;
import com.hust.service.AuthorizationService;
import com.hust.utils.Result;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.UUID;

import static com.hust.utils.CodeUtils.parseCode;
import static com.hust.utils.Conversion.toAppPO;
import static com.hust.utils.Conversion.toUserPO;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    @Autowired
    private AuthorizationMapper authorizationMapper;

    @Override
    public Result createApp(AppDTO appDTO) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        AppPO appPO = toAppPO(appDTO);
        appPO.setClientId(uuid);
        int rowsAffected = authorizationMapper.createApp(appPO);
        if (rowsAffected > 0) {
            return Result.success(uuid);
        } else {
            return Result.error();
        }
    }

    @Override
    public Result authorize(AuthorizeDTO authorizeDTO) {
        AppPO appPO = toAppPO(authorizeDTO);
        AppPO filterResult = authorizationMapper.filterState(appPO);
        if (filterResult == null) {
            return Result.error();
        }
        int rowsAffected = authorizationMapper.insertState(authorizeDTO.getState());
        if (rowsAffected > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @Override
    public Result verifyClientInfo(TokenDTO tokenDTO) {
        byte[] decodedBytes = Base64.getDecoder().decode(tokenDTO.getCode());
        String code = new String(decodedBytes);
        tokenDTO.setCode(code);
        AppPO appPO = toAppPO(tokenDTO);
        AppPO app = authorizationMapper.verifyClient(appPO);
        Claims claims = parseCode(tokenDTO.getCode());
        ResourcePO resourcePO = authorizationMapper.verifyCode((String) claims.get("code"));
        if (app == null || resourcePO == null) {
            return Result.error();
        } else {
            return Result.success();
        }
    }

    @Override
    public Result storageToken(Token token) {
        // 等会要加好多好多的try-catch，赶紧搞吧
        Claims claims = parseCode(token.getCode());
        token.setCode((String) claims.get("code"));
        int rowsAffected = authorizationMapper.updateToken(token);
        if (rowsAffected > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }
}
