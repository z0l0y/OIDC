package com.hust.service;

import com.hust.dto.AppDTO;
import com.hust.dto.AuthorizeDTO;
import com.hust.utils.Result;

public interface AuthorizationService {
    Result createApp(AppDTO appDTO);

    Result authorize(AuthorizeDTO authorizeDTO);
}
