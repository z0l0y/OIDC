package com.hust.service.Impl;

import com.hust.mapper.ClientMapper;
import com.hust.service.ClientService;
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
}
