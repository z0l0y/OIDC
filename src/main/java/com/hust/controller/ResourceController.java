package com.hust.controller;

import com.hust.dto.AccessTokenDTO;
import com.hust.dto.VerifyDTO;
import com.hust.pojo.Code;
import com.hust.pojo.Token;
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

import static com.hust.utils.StorageUtils.storageCode;

@RestController
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    /**
     * 4，接下来这个接口是用来在GitHub（类似）页面跳转后的那个授权页面，就是那个有密码的，如果授权成功那么就会发放code
     * 注意这里我们把code存在了resource_info里面，便于我们写后面的取数据的逻辑
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
            Code storageCode = storageCode(verifyDTO, uuidCode);
            resourceService.storageCode(storageCode);
            return Result.success(base64Code);
        } else {
            return Result.error("用户信息验证失败，授权失败！");
        }
    }

    /**
     * 6.目前看来就只有state是在第三方得到的，其他的逻辑都在我们信任的资源服务器和授权服务器这边，现在看来确实安全很多
     * 最后一步，就是解析获取到的Base64编码，得到token进行了（除了client_state，其他的都是在我们这边的表，所以第三方也只能拿到state数据，其他的都是拿不到的，所以很安全）
     *
     * @param accessTokenDTO 第5步获取到的token（Base64编码格式）
     * @return 用户的信息
     */
    @PostMapping("/get/userInfo")
    public Result getUserInfo(@RequestBody AccessTokenDTO accessTokenDTO) {
        return resourceService.getUserInfo(accessTokenDTO);
    }
}
