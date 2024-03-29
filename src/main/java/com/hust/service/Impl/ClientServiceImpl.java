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
import static com.hust.utils.ExamineTokenExpire.examineToken;
import static com.hust.utils.StateUtils.parseState;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientMapper clientMapper;

    @Override
    public void storeState(String state) {
        // 避免存储重复的state
        Object existence = clientMapper.verify(state);
        if (existence != null) {
            return;
        }
        clientMapper.storeState(state);
    }

    @Override
    public Result verifyState(StateDTO stateDTO) {
        byte[] decodedBytes = new byte[0];
        String state;
        try {
            decodedBytes = Base64.getDecoder().decode(stateDTO.getState());
            state = new String(decodedBytes);
            Claims claims = parseState(state);
        } catch (RuntimeException e) {
            return Result.error("state被修改，检测到CSRF攻击!");
        }
        try {
            examineToken(state);
        } catch (RuntimeException e) {
            return Result.error("state已失效，请重新进行授权验证！");
        }

        StatePO statePO = clientMapper.verifyState(stateDTO.getState());
        if (statePO == null) {
            return Result.error("state无效，请重新进行授权验证！");
        } else {
            return Result.success("state验证成功，可以进行下面的逻辑！");
        }
    }
}
