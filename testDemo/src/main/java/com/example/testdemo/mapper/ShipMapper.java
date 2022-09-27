package com.example.testdemo.mapper;

import com.example.testdemo.entity.Ship;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipMapper {

    @Insert("insert into sustc.public.ship (name,company_id) values (#{name},#{companyId}) on conflict do nothing")
    void add(Ship ship);

    @Select("select id from sustc.public.ship where name = #{name}")
    int getId(Ship ship);

    @Delete("delete from sustc.public.ship")
    void deleteAll();


}
