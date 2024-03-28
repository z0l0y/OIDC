package com.hust.service.Impl;

import com.hust.dto.StateDTO;
import com.hust.mapper.ClientMapper;
import com.hust.po.StatePO;
import com.hust.service.ClientService;
import com.hust.utils.Result;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

import static com.hust.utils.AccessTokenUtils.parseAccessToken;
import static com.hust.utils.StateUtils.parseState;

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
        byte[] decodedBytes = new byte[0];
        try {
            decodedBytes = Base64.getDecoder().decode(stateDTO.getState());
            String state = new String(decodedBytes);
            Claims claims = parseState(state);
        } catch (RuntimeException e) {
            return Result.error("state已过期或无效，请重新进行授权验证！");
        }

        StatePO statePO = clientMapper.verifyState(stateDTO.getState());
        if (statePO == null) {
            return Result.error("state无效，请重新进行授权验证！");
        } else {
            return Result.success("state验证成功，可以进行下面的逻辑！");
        }
    }
}
