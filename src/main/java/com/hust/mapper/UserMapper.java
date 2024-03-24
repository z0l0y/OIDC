package com.hust.mapper;

import com.hust.po.UserPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    /**
     * 创建用户.
     *
     * @param userPO 用户传输对象，包含用户信息
     * @return 创建用户是否成功，1代表成功，0代表失败
     */
    @Insert("insert into user_info (username, password, email, nickname, avatar, bio) " +
            "values (#{username}, #{password}, #{email}, #{nickname}, #{avatar}, #{bio})")
    int createUser(UserPO userPO);
}
