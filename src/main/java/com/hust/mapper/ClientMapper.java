package com.hust.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.hibernate.validator.constraints.ru.INN;

@Mapper
public interface ClientMapper {
    @Insert("insert into client_state (state) values (#{state})")
    void storeState(String state);
}
