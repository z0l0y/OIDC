package com.hust.utils;

import com.hust.dto.VerifyDTO;
import com.hust.pojo.Code;
import com.hust.pojo.Token;

public class StorageUtils {
    public static Code storageCode(VerifyDTO verifyDTO, String uuidCode) {
        Code code = new Code();
        code.setUsername(verifyDTO.getUsername());
        code.setPassword(verifyDTO.getPassword());
        code.setCode(uuidCode);
        return code;
    }

    public static Token storageToken(String uuidAccessToken, String uuidRefreshToken, String code) {
        Token token = new Token();
        token.setCode(code);
        token.setAccessToken(uuidAccessToken);
        token.setRefreshToken(uuidRefreshToken);
        return token;
    }

}
