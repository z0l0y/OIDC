package com.hust.service;

import com.hust.dto.StateDTO;
import com.hust.utils.Result;

public interface ClientService {
    void storeState(String state);

    Result verifyState(StateDTO stateDTO);
}
