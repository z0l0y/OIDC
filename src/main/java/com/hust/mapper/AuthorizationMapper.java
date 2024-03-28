package com.hust.mapper;

import com.hust.po.AppPO;
import com.hust.po.ResourcePO;
import com.hust.po.UserPO;
import com.hust.pojo.Token;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AuthorizationMapper {
    @Insert("insert into client_info (name, client_id, client_secret, redirect_url) " +
            "values (#{name}, #{clientId}, #{clientSecret}, #{redirectUrl})")
    int createApp(AppPO appPO);

    @Select("select * from oidc.client_info where client_id = #{clientId} and redirect_url = #{redirectUrl}")
    AppPO filterState(AppPO appPO);

    @Insert("insert into authorization_state (state) values (#{state})")
    int insertState(String state);

    @Insert("update authorization_state set scope = #{scope} where state = #{state}")
    int updateScope(String scope, String state);

    @Select("select * from oidc.client_info where client_id = #{clientId} and client_secret = #{clientSecret}")
    AppPO verifyClient(AppPO appPO);

    @Select("select * from oidc.resource_info where code = #{code}")
    ResourcePO verifyCode(String code);

    @Update("update resource_info set access_token = #{accessToken}, refresh_token = #{refreshToken} where code= #{code}")
    int updateToken(Token token);
}
