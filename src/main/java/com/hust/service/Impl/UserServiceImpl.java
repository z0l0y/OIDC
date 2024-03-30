package com.hust.service.Impl;

import com.hust.dto.UserDTO;
import com.hust.mapper.UserMapper;
import com.hust.po.UserPO;
import com.hust.service.UserService;
import com.hust.utils.Result;
import com.hust.validator.UserValidator;
import com.hust.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import java.security.GeneralSecurityException;

import static com.hust.utils.Constants.*;
import static com.hust.utils.Conversion.toUserPO;
import static com.hust.utils.Conversion.toUserVO;
import static com.hust.utils.MD5.generatePasswordMD5;
import static com.hust.utils.SendEmail.sendEmail;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 采用userValidator，在正式进入业务逻辑之前先过滤一下非法的userDTO
     */
    private UserValidator userValidator;

    @Autowired
    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    /**
     * 创建用户.
     *
     * @param userDTO 用户传输对象，包含用户信息
     * @return 创建用户是否成功，true表示创建成功，false表示创建失败
     */
    @Override
    public Result createUser(UserDTO userDTO) throws GeneralSecurityException, MessagingException {
        // 进入业务逻辑之前一定要对我们的数据进行检测，不要污染了数据库
        if (userValidator.UserInfoValidator(userDTO).getCode() == 0) {
            return Result.error(INVALID_DATA_ERROR_MESSAGE);
        }
        if (userDTO.getEmail().matches("([a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6})")) {
            generatePasswordMD5(userDTO);
        }
        UserPO userPO = toUserPO(userDTO);
        Object verifyCode = userMapper.verifyCode(userDTO.getEmail(), userDTO.getCode());
        if (verifyCode == null) {
            return Result.error();
        }
        Object usernameExistence = userMapper.uniqueUser(userPO.getUsername());
        if (usernameExistence != null) {
            return Result.error("该用户名已经被占用！请换一个用户名！");
        }
        int rowsAffected = userMapper.createUser(userPO);
        if (rowsAffected > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @Override
    public void storageCode(UserDTO userDTO) {
        userMapper.storageCode(userDTO);
    }

    @Override
    public Result loginBangumi(UserDTO userDTO) {
        UserPO userPO = toUserPO(userDTO);
        UserPO user = userMapper.getUser(userPO);
        if (user == null) {
            return Result.error();
        } else {
            UserVO userVO = toUserVO(user);
            return Result.success(userVO);
        }
    }

    @Override
    public Result updateUserInfo(UserDTO userDTO) {
        UserPO userPO = toUserPO(userDTO);
        int number = userMapper.updateUserInfo(userPO);
        if (number > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @Override
    public Result getUserProfile(UserDTO userDTO) {
        UserPO userPO = toUserPO(userDTO);
        UserPO userProfile = userMapper.getUserProfile(userPO);
        if (userProfile == null) {
            return Result.error();
        } else {
            UserVO userVO = toUserVO(userProfile);
            return Result.success(userVO);
        }
    }

}
