package com.example.testdemo.mapper;

import com.example.testdemo.entity.City;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface CityMapper {
    @Insert("insert into sustc.public.city (name) values (#{name}) on conflict do nothing")
    void add(City city);

    @Select("select id from sustc.public.city where name = #{name}")
    int getId(String name);

    @Delete("delete from sustc.public.city")
    void deleteAll();

}
