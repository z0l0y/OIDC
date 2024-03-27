package com.hust.mapper;

import com.hust.po.ResourcePO;
import com.hust.pojo.Code;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ResourceMapper {
    @Select("select * from oidc.resource_info where username= #{username} and password= #{password}")
    ResourcePO verifyIdentity(ResourcePO resourcePO);

    @Update("update resource_info set code = #{code} where username= #{username} and password= #{password}")
    int updateCode(Code code);
}
