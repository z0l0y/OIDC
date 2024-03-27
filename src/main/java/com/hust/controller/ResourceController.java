package com.hust.controller;

import com.hust.dto.VerifyDTO;
import com.hust.service.ResourceService;
import com.hust.utils.CodeUtils;
import com.hust.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    /**
     * 4，接下来这个接口是用来在GitHub（类似）页面跳转后的那个授权页面，就是那个有密码的，如果授权成功那么就会发放code
     *
     * @param verifyDTO 存储的是用户的基本信息，比如username和password，由我们信任的资源服务器来接受我们的信息，这一块不在第三方进行
     * @return 验证用户信息成功后会发放code令牌，注意时效性
     */
    @PostMapping("/verify/user/identity")
    public Result verifyUserIdentity(@RequestBody VerifyDTO verifyDTO) {
        // 你在这里不搞，那么我们等下得到access token之后要取哪一个用户的数据呢？是不是有一点拔剑四顾心茫然啊
        Result verifyPass = resourceService.verifyIdentity(verifyDTO);
        if (verifyPass.getCode() == 1) {
            // 登录成功，生成令牌，下发令牌
            Map<String, Object> claims = new HashMap<>();
            String uuidCode = UUID.randomUUID().toString().replace("-", "");
            claims.put("code", uuidCode);
            String code = CodeUtils.generateCode(claims);
            String base64Code = Base64.getEncoder().encodeToString(code.getBytes());
            return Result.success(base64Code);
        } else {
            return Result.error("用户信息验证失败，授权失败！");
        }
    }
}
