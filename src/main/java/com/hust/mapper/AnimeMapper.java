package com.hust.mapper;

import com.hust.po.AnimePO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

@Mapper
public interface AnimeMapper {
    @Select("select * from oidc.anime where name = #{name} and is_deleted = 0")
    AnimePO verifyAnimeExist(AnimePO animePO);

    @Insert("insert into anime (name, episodes, director, avatar, introduction) " +
            "values (#{name}, #{episodes}, #{director}, #{avatar}, #{introduction})")
    int addAnime(AnimePO animePO);

    @Select("select * from oidc.anime where is_deleted = 0")
    AnimePO[] getAllAnime();

    @Update("update anime set episodes = #{episodes} where name = #{name} and is_deleted = 0")
    int updateAnime(AnimePO animePO);

    @Update("update anime set is_deleted = 1 where name = #{name}")
    int deleteAnime(AnimePO animePO);
}
