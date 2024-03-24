package com.hust.controller;

import com.hust.dto.UserDTO;
import com.hust.service.UserService;
import com.hust.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.*;
import java.security.GeneralSecurityException;

import static com.hust.utils.Constants.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result createUser(@RequestBody UserDTO userDTO) throws GeneralSecurityException, MessagingException {

        final Result isCreteUser = userService.createUser(userDTO);
        if (isCreteUser.getData() == INVALID_DATA_ERROR_MESSAGE) {
            return Result.error(INVALID_DATA_ERROR_MESSAGE);
        }
        if (isCreteUser.getData() == REQUEST_TOO_FREQUENT_MESSAGE) {
            return Result.error(REQUEST_TOO_FREQUENT_MESSAGE);
        }
        if (isCreteUser.getCode() == 1) {
            return Result.success(CREATE_SUCCESS_MESSAGE);
        } else {
            return Result.error(CREATE_FAILURE_MESSAGE);
        }
    }
}
