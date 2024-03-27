package com.hust.service.Impl;

import com.hust.dto.StateDTO;
import com.hust.mapper.ClientMapper;
import com.hust.po.StatePO;
import com.hust.service.ClientService;
import com.hust.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientMapper clientMapper;

    @Override
    public void storeState(String state) {
        clientMapper.storeState(state);
    }

    @Override
    public Result verifyState(StateDTO stateDTO) {
        StatePO statePO = clientMapper.verifyState(stateDTO.getState());
        if (statePO == null) {
            return Result.error();
        } else {
            return Result.success();
        }
    }
}
