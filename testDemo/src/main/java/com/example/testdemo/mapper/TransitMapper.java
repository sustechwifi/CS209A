package com.example.testdemo.mapper;

import com.example.testdemo.entity.Transit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface TransitMapper {
    @Insert("insert into sustc.public.transit (type, time, tax, city_id, record_id) VALUES " +
            "(#{type},#{time},#{tax},#{cityId},#{recordId}) on conflict do nothing ")
    void add(Transit transit);

    @Delete("delete from sustc.public.transit")
    void deleteAll();


    Transit getTransitsByModel(Transit transit);


    List<Transit> getTransitsByRecordId(@Param("id") int id);

    List<Integer> getRecordIdByDateAndType(@Param("date") Date date, @Param("type") int type);

    List<Integer> getRecordIdByTaxAndType(@Param("tax") Double tax, @Param("type") int type);

    void deleteByRecordIds(Integer[] ids);

    void addBatch(List<Transit> list);


    Map<String, Double> getResult3();
}

