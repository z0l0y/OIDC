package com.hust.controller;

import com.hust.dto.UserDTO;
import com.hust.service.UserService;
import com.hust.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 减少魔法值，使得代码的可读性增强
     */
    private static final String CREATE_SUCCESS_MESSAGE = "创建用户成功！";
    private static final String CREATE_FAILURE_MESSAGE = "创建用户失败！";

    @PostMapping("/register")
    public Result createUser(@RequestBody UserDTO userDTO) {
        final boolean isCreteUser = userService.createUser(userDTO);

        if (isCreteUser) {
            return Result.success(CREATE_SUCCESS_MESSAGE);
        } else {
            return Result.error(CREATE_FAILURE_MESSAGE);
        }
    }
}
