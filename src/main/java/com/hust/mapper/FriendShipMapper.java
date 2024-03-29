package com.hust.mapper;

import com.hust.dto.FriendDTO;
import com.hust.po.FriendShipPO;
import com.hust.po.RatingPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface FriendShipMapper {
    @Select("select * from oidc.friendship where user1 = #{user1} and user2 = #{user2}")
    FriendShipPO[] verifyApplication(FriendDTO friendDTO);

    @Insert("insert into friendship (user1, user2) values (#{user1}, #{user2})")
    int applyFriend(FriendDTO friendDTO);

    @Select("select * from oidc.friendship where user2 = #{user2} and status = 0")
    FriendShipPO[] seePendingMsg(FriendDTO friendDTO);

    @Update("update friendship set status = 1 where user1 = #{user1} and user2 = #{user2}")
    void agreeFriend(FriendDTO friendDTO);

    @Update("update friendship set status = -1 where user1 = #{user1} and user2 = #{user2}")
    void disagreeFriend(FriendDTO friendDTO);

    @Select("select * from oidc.friendship where user2 = #{user2} and user1 = #{user1} and status = 1")
    FriendShipPO verifyFriend1(FriendDTO friendDTO);

    @Select("select * from oidc.friendship where user2 = #{user1} and user1 = #{user2} and status = 1")
    FriendShipPO verifyFriend2(FriendDTO friendDTO);

    @Select("select * from oidc.rating where username = #{user1} order by gmt_modified desc")
    List<RatingPO> notifyUser2(String user1);

    @Select("select * from oidc.friendship where user2 = #{username} and status = 1")
    FriendShipPO[] showMyAllFriends(String username);
}
