package com.hust.service;

import com.hust.dto.AccessTokenDTO;
import com.hust.dto.VerifyDTO;
import com.hust.pojo.Code;
import com.hust.utils.Result;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface ResourceService {
    Result verifyIdentity(VerifyDTO verifyDTO);

    Result storageCode(Code code);

    Result getUserInfo(AccessTokenDTO accessTokenDTO, String idToken) throws ParseException, JOSEException;
}
