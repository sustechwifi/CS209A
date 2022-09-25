package com.example.testdemo.mapper;

import com.example.testdemo.entity.Courier;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface CourierMapper {

    @Insert("insert into sustc.public.courier (phone_number, name, age, gender, company_id, city_id) " +
            "values (#{phoneNumber},#{name},#{age},#{gender},#{companyId},#{cityId}) on conflict do nothing  returning id")
    int add(Courier courier);

    @Select("select id from sustc.public.courier where name = #{name} and phone_number = #{phoneNumber}")
    int getId(Courier courier);
}
