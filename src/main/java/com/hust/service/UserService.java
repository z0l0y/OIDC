package com.hust.service;

import com.hust.dto.UserDTO;

public interface UserService {
    /**
     * 创建用户.
     *
     * @param userDTO 用户传输对象，包含用户信息
     * @return 创建用户是否成功，true表示创建成功，false表示创建失败
     */
    boolean createUser(UserDTO userDTO);

}
