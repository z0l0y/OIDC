package com.hust.mapper;

import com.hust.po.CollectionPO;
import com.hust.po.RatingPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CollectionMapper {
    @Insert("insert into collection (username, anime_name, type) values (#{username}, #{animeName}, #{type})")
    int insertCollection(CollectionPO collectionPO);

    @Insert("insert into rating (rating_value, anime_name, commentary) values (#{ratingValue}, #{animeName}, #{commentary})")
    int insertRating(RatingPO ratingPO);

    @Select("select * from oidc.collection where username = #{username}")
    CollectionPO[] showMyAllCollections(String username);

    @Select("select * from oidc.collection where username = #{username} and anime_name LIKE CONCAT('%', #{keyword}, '%')")
    CollectionPO[] searchMyCollections(String username, String keyword);
}
