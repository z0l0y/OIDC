package com.hust.controller;

import com.hust.dto.AuthorizeDTO;
import com.hust.dto.UserDTO;
import com.hust.service.UserService;
import com.hust.utils.JwtUtils;
import com.hust.utils.Result;
import com.hust.utils.UploadUtil;
import com.hust.vo.UserVO;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import static com.hust.utils.Constants.*;
import static com.hust.utils.JwtUtils.parseJWT;
import static com.hust.utils.RandomCodeGenerator.generateRandomCode;
import static com.hust.utils.SendEmail.sendEmail;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/code")
    public Result getCode(@RequestBody UserDTO userDTO) {
        String code = generateRandomCode();
        userDTO.setCode(code);
        try {
            if (sendEmail(userDTO).getCode() == 0) {
                return Result.error(REQUEST_TOO_FREQUENT_MESSAGE);
            }
            userService.storageCode(userDTO);
        } catch (MessagingException | GeneralSecurityException e) {
            e.printStackTrace();
            return Result.error("发送邮件失败！");
        }
        return Result.success("发送邮件成功！");
    }

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
            //登录成功，生成令牌，下发令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", userDTO.getUsername());
            claims.put("password", userDTO.getPassword());

            String token = JwtUtils.generateJwt(claims);
            return Result.success(token);
        } else {
            // 登录失败，放回错误信息
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
        // 要求登录后才能修改信息，主要是你没有注册我也不会把你修改的数据存储起来
        Result updateResult = userService.updateUserInfo(userDTO);
        if (updateResult.getCode() == 1) {
            return Result.success("修改信息成功！");
        } else {
            return Result.error("修改信息失败！");
        }
    }

    @GetMapping("/profile")
    public Result getUserProfile(@RequestHeader("Authorization") String authorizationHeader) {
        // 提取JWT令牌的内容
        String token = authorizationHeader.substring(7); // 去除"Bearer "前缀
        Claims claims = parseJWT(token);
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername((String) claims.get("username"));
        // 注意不要直接ctrl+D要不然注意了后面的参数，前面的方法没有改，会造成恶性BUG，很难检查出来
        userDTO.setPassword((String) claims.get("password"));
        return userService.getUserProfile(userDTO);
    }

}
