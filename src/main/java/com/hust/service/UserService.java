package com.hust.service;

import com.hust.dto.UserDTO;
import com.hust.utils.Result;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;

public interface UserService {
    /**
     * 创建用户.
     *
     * @param userDTO 用户传输对象，包含用户的username，password，nickname和Email
     * @return 创建用户是否成功，true表示创建成功，false表示创建失败
     */
    Result createUser(UserDTO userDTO) throws MessagingException, GeneralSecurityException;

    /**
     * @param userDTO 登录传输对象，包含用户的username和password
     * @return 登录是否成功，true表示登录成功，false表示登录失败
     */
    Result loginBangumi(UserDTO userDTO);
}
