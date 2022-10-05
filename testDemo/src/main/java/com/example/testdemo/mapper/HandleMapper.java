package com.example.testdemo.mapper;

import com.example.testdemo.entity.Handle;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HandleMapper {
    @Insert("insert into sustc.public.handle (type, time, record_id, courier_id) VALUES (#{type},#{time},#{recordId},#{courierId}) on conflict do nothing ")
    void add(Handle handle);

    @Delete("delete from sustc.public.handle")
    void deleteAll();

    List<Integer> getRecordIdsByCourierIdAndType(@Param("id") Integer id, @Param("type") Integer type);

    List<Handle> getHandlesByRecordId(int id);

    void deleteByRecordIds(Integer[] ids);

    void addByBatch(List<Handle> list);
}
