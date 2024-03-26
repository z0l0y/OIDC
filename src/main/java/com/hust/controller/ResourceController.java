package com.hust.controller;

import com.hust.dto.VerifyDTO;
import com.hust.service.ResourceService;
import com.hust.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @PostMapping("/verify/user/identity")
    public Result verifyUserIdentity(@RequestBody VerifyDTO verifyDTO) {
        // 你在这里不搞，那么我们等下得到access token之后要取哪一个用户的数据呢？是不是有一点拔剑四顾心茫然啊
        Result verifyPass = resourceService.verifyIdentity(verifyDTO);
        if (verifyPass.getCode() == 1) {
            return Result.success("验证通过！");
        } else {
            return Result.error("验证失败！");
        }
    }
}
