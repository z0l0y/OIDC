package com.hust.service;

import com.hust.dto.VerifyDTO;
import com.hust.utils.Result;

public interface ResourceService {
    Result verifyIdentity(VerifyDTO verifyDTO);
}
