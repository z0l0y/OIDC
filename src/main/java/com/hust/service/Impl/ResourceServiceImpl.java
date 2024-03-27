package com.hust.service.Impl;

import com.hust.dto.VerifyDTO;
import com.hust.mapper.ResourceMapper;
import com.hust.po.AppPO;
import com.hust.po.ResourcePO;
import com.hust.pojo.Code;
import com.hust.service.ResourceService;
import com.hust.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hust.utils.Conversion.toAppPO;
import static com.hust.utils.Conversion.toResourcePO;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public Result verifyIdentity(VerifyDTO verifyDTO) {
        ResourcePO resourcePO = toResourcePO(verifyDTO);
        ResourcePO resourcePO1 = resourceMapper.verifyIdentity(resourcePO);
        if (resourcePO1 != null) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @Override
    public Result storageCode(Code code){
        resourceMapper.updateCode(code);
        int rowsAffected = resourceMapper.updateCode(code);
        if (rowsAffected > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }
}
