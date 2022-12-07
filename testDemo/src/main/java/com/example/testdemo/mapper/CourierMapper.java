package com.example.testdemo.mapper;

import com.example.testdemo.entity.Courier;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface CourierMapper {

    @Insert("insert into sustc.public.courier (phone_number, name, birth_year, gender, company_id, city_id) " +
            "values (#{phoneNumber},#{name},#{age},#{gender},#{companyId},#{cityId}) on conflict do nothing")
    void add(Courier courier);


    Integer getId(@Param("courier") Courier courier);

    @Delete("delete from sustc.public.courier")
    void deleteAll();


    Courier selectById(int id);

    Courier getCourier(@Param("courier") Courier courier);

    Map<String, Integer> getGreatestCourier(@Param("city") String city, @Param("company") String company);
}
