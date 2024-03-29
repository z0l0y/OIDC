package com.hust.service;

import com.hust.dto.AppDTO;
import com.hust.dto.AuthorizeDTO;
import com.hust.dto.TokenDTO;
import com.hust.pojo.Token;
import com.hust.utils.Result;

import java.util.HashMap;

public interface AuthorizationService {
    Result createApp(AppDTO appDTO);

    Result authorize(AuthorizeDTO authorizeDTO);

    Result verifyClientInfo(TokenDTO tokenDTO);

    Result storageToken(Token token);

    HashMap<Object, Object> getUserInfo(String name, String password, String scope);
}
