package com.example.testdemo.mapper;

import com.example.testdemo.entity.Courier;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface CourierMapper {

    @Insert("insert into sustc.public.courier (phone_number, name, age, gender, company_id, city_id) " +
            "values (#{phoneNumber},#{name},#{age},#{gender},#{companyId},#{cityId}) on conflict do nothing")
    void add(Courier courier);


    Integer getId(@Param("courier") Courier courier);

    @Delete("delete from sustc.public.courier")
    void deleteAll();


    Courier selectById(int id);

    Courier getCourier(@Param("courier") Courier courier);
}
