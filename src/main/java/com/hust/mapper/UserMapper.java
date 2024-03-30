package com.hust.mapper;

import com.hust.dto.UserDTO;
import com.hust.po.UserPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Insert("insert into mail_code (email, code) values (#{email}, #{code})")
    void storageCode(UserDTO userDTO);

    @Select("select * from oidc.mail_code where email = #{email} and code = #{code}")
    Object verifyCode(String email, String code);

    @Select("select * from oidc.user_info where username = #{username}")
    Object uniqueUser(String username);

    /**
     * 创建用户.
     *
     * @param userPO 用户传输对象，包含用户信息
     * @return 创建用户是否成功，1代表成功，0代表失败
     */
    @Insert("insert into user_info (username, password, email, nickname, bio) " +
            "values (#{username}, #{password}, #{email}, #{nickname}, #{bio})")
    int createUser(UserPO userPO);

    /**
     * @param userPO 用户传输对象，包含用户基本信息
     * @return 获取用户的信息
     */
    @Select("select * from oidc.user_info " +
            "where username = #{username} and password = #{password}")
    UserPO getUser(UserPO userPO);

    /**
     * @param userPO 用户传输对象，包含修改用户信息的基本信息
     * @return 修改用户信息是否成功，1代表成功，0代表失败
     */
    int updateUserInfo(UserPO userPO);

    @Select("select * from oidc.user_info where username = #{username} and password = #{password}")
    UserPO getUserProfile(UserPO userPO);
}
