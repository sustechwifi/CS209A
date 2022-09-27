package com.example.testdemo.mapper;

import com.example.testdemo.entity.Transit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface TransitMapper {
    @Insert("insert into sustc.public.transit (type, time, tax, city_id, record_id) VALUES " +
            "(#{type},#{time},#{tax},#{cityId},#{recordId}) on conflict do nothing ")
    void add(Transit transit);

    @Delete("delete from sustc.public.transit")
    void deleteAll();
}
