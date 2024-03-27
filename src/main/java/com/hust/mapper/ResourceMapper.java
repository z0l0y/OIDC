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

    @Select("select * from oidc.resource_info where access_token = #{accessToken}")
    ResourcePO verifyAccessToken(String accessToken);

    @Select("select * from oidc.resource_info where refresh_token = #{refreshToken}")
    ResourcePO verifyRefreshToken(String refreshToken);

    @Update("update resource_info set access_token = #{accessToken} where refresh_token = #{refreshToken}")
    int updateAccessToken(String accessToken, String refreshToken);

    @Select("select * from oidc.resource_info where access_token = #{accessToken}")
    ResourcePO getUserInfo(String accessToken);
}
