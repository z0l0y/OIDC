package com.hust.service.Impl;

import com.hust.dto.UserDTO;
import com.hust.mapper.UserMapper;
import com.hust.po.UserPO;
import com.hust.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 采用validator，在正式进入业务逻辑之前先过滤一下非法的userDTO
     */
    private Validator validator;

    @Autowired
    public void UserService(Validator validator) {
        this.validator = validator;
    }

    /**
     * 创建用户.
     *
     * @param userDTO 用户传输对象，包含用户信息
     * @return 创建用户是否成功，true表示创建成功，false表示创建失败
     */
    @Override
    public boolean createUser(UserDTO userDTO) {
        // 进入业务逻辑之前一定要对我们的数据进行检测，不要污染了数据库
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<UserDTO> violation : violations) {
                System.out.println(violation.getMessage());
            }
            return false;
        }
        UserPO userPO = toUserPO(userDTO);
        int rowsAffected = userMapper.createUser(userPO);
        return rowsAffected > 0;
    }

    /**
     * 将用户传输对象转换为用户持久化对象.
     *
     * @param userDTO 用户传输对象，包含用户信息
     * @return 转换后的用户持久化对象UserPO
     */
    private UserPO toUserPO(UserDTO userDTO) {
        UserPO userPO = new UserPO();
        userPO.setUsername(userDTO.getUsername());
        userPO.setPassword(userDTO.getPassword());
        userPO.setEmail(userDTO.getEmail());
        userPO.setNickname(userDTO.getNickname());
        userPO.setAvatar(userDTO.getAvatar());
        userPO.setBio(userDTO.getBio());
        return userPO;
    }
}
