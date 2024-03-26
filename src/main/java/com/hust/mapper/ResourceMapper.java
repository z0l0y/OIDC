package com.hust.mapper;

import com.hust.po.ResourcePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ResourceMapper {
    @Select("select * from oidc.resource_info where username= #{username} and password= #{password}")
    ResourcePO verifyIdentity(ResourcePO resourcePO);
}
