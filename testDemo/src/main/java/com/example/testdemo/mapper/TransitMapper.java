package com.example.testdemo.mapper;

import com.example.testdemo.entity.Transit;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface TransitMapper {
    @Insert("insert into sustc.public.transit (type, time, tax, city_id, record_id) VALUES " +
            "(#{type},#{time},#{tax},#{cityId},#{recordId})")
    void add(Transit transit);
}
