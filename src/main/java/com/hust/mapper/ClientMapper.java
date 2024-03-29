package com.hust.mapper;

import com.hust.po.StatePO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.hibernate.validator.constraints.ru.INN;

@Mapper
public interface ClientMapper {
    @Select("select * from oidc.client_state where state = #{state}")
    Object verify(String state);

    @Insert("insert into client_state (state) values (#{state})")
    void storeState(String state);

    @Select("select * from oidc.client_state where state = #{state}")
    StatePO verifyState(String state);
}
