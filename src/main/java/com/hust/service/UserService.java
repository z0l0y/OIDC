package com.hust.service;

import com.hust.dto.UserDTO;
import com.hust.utils.Result;
import com.hust.vo.UserVO;

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

    /**
     * @param userDTO 传输对象，包含了唯一标识符username和我们要进行修改的nickname（因为要考虑到我们可能有用一个邮箱注册多个账户的人，所以只能选username来作为参照了）
     * @return 修改用户nickname是否成功
     */
    // TODO 我们这里要考虑重名的问题，这样查出来才不会是两个用户
    Result updateUserInfo(UserDTO userDTO);

    Result getUserProfile(UserDTO userDTO);
}
