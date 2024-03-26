package com.hust.service;

import com.hust.dto.AppDTO;
import com.hust.utils.Result;

public interface AuthorizationService {
    Result createApp(AppDTO appDTO);
}
