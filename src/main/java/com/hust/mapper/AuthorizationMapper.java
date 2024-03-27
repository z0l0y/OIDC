package com.hust.mapper;

import com.hust.po.AppPO;
import com.hust.po.ResourcePO;
import com.hust.po.UserPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AuthorizationMapper {
    @Insert("insert into client_info (name, client_id, client_secret, redirect_url) " +
            "values (#{name}, #{clientId}, #{clientSecret}, #{redirectUrl})")
    int createApp(AppPO appPO);

    /*    AppPO getAppInfo()*/
    @Update("update client_info set state = #{state} where client_id = #{clientId} and redirect_url = #{redirectUrl}")
    int insertState(AppPO appPO);

    @Select("select * from oidc.client_info where client_id = #{clientId} and client_secret = #{clientSecret} and code = #{code}")
    AppPO verify(AppPO appPO);
}
