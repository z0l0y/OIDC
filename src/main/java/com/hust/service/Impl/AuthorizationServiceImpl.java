package com.hust.service.Impl;

import com.hust.dto.AppDTO;
import com.hust.dto.AuthorizeDTO;
import com.hust.mapper.AuthorizationMapper;
import com.hust.po.AppPO;
import com.hust.po.UserPO;
import com.hust.service.AuthorizationService;
import com.hust.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.UUID;

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
    public Result authorize(AuthorizeDTO authorizeDTO){
        AppPO appPO = toAppPO(authorizeDTO);
        int rowsAffected = authorizationMapper.insertState(appPO);
        if (rowsAffected > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }
}
