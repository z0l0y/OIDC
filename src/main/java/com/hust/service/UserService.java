package com.hust.service;

import com.hust.dto.UserDTO;
import com.hust.utils.Result;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;

public interface UserService {
    /**
     * 创建用户.
     *
     * @param userDTO 用户传输对象，包含用户信息
     * @return 创建用户是否成功，true表示创建成功，false表示创建失败
     */
    Result createUser(UserDTO userDTO) throws MessagingException, GeneralSecurityException;

}
