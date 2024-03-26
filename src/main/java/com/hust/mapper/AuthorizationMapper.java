package com.hust.mapper;

import com.hust.po.AppPO;
import com.hust.po.UserPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthorizationMapper {
    @Insert("insert into client_info (name, client_id, client_secret, redirect_url) " +
            "values (#{name}, #{clientId}, #{clientSecret}, #{redirectUrl})")
    int createApp(AppPO appPO);
}
