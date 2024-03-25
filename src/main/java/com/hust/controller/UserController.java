package com.hust.controller;

import com.hust.dto.UserDTO;
import com.hust.service.UserService;
import com.hust.utils.Result;
import com.hust.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.*;
import java.io.IOException;
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

    @PostMapping("/login")
    public Result loginBangumi(@RequestBody UserDTO userDTO) {
        Result isLoginBangumi = userService.loginBangumi(userDTO);
        if (isLoginBangumi.getData() == INVALID_DATA_ERROR_MESSAGE) {
            return Result.error(INVALID_DATA_ERROR_MESSAGE);
        }
        if (isLoginBangumi.getCode() == 1) {
            return isLoginBangumi;
        } else {
            return Result.error(LOGIN_FAILURE);
        }
    }

    @PostMapping("/upImg")
    public String upImg(MultipartFile file) throws IOException {
        return UploadUtil.uploadImage(file);
    }

    // TODO 用户可能只改一个信息，比如nickname或者是bio，那么我们这里最好使用动态SQL来实现
    @PostMapping("/update")
    public Result updateUserInfo(@RequestBody UserDTO userDTO) {
        // 首先我们要考虑，有什么信息是要update的，一般就是nickname和bio
        Result updateResult = userService.updateUserInfo(userDTO);
        if (updateResult.getCode() == 1) {
            return Result.success("修改信息成功！");
        } else {
            return Result.error("修改信息失败！");
        }
    }

}
