package com.example.testdemo.mapper;

import com.example.testdemo.entity.Record;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordMapper {

    @Insert("insert into sustc.public.record (log_time, item_name, item_class, item_price, company_id, container_id, ship_id, state) " +
            "VALUES (#{logTime},#{itemName},#{itemClass},#{itemPrice},#{companyId},#{containerId},#{shipId},#{state}) on conflict do nothing")
    int add(Record record);

    @Select("select id from sustc.public.record where item_name = #{itemName}")
    int getId(Record record);

    @Delete("delete from sustc.public.record")
    void deleteAll();

    List<Record> getRecordsByModel(@Param("record") Record record, @Param("begin") int begin, @Param("size") int size);


    List<Integer> getIds(@Param("record") Record record);


    Record getRecordById(@Param("id") int id);

    List<Record> getRecordByIds(@Param("ids") Integer[] ids);
}
