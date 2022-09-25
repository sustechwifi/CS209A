package com.example.testdemo.mapper;

import com.example.testdemo.entity.Handle;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface HandleMapper {
    @Insert("insert into sustc.public.handle (type, time, record_id, courier_id) VALUES (#{type},#{time},#{recordId},#{courierId})")
    void add(Handle handle);
}
