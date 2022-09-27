package com.example.testdemo.mapper;

import com.example.testdemo.entity.Record;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordMapper {

    @Insert("insert into sustc.public.record (log_time, item_name, item_class, item_price, company_id, container_id, ship_id) " +
            "VALUES (#{logTime},#{itemName},#{itemClass},#{itemPrice},#{companyId},#{containerId},#{shipId}) on conflict do nothing")
    int add(Record record);

    @Select("select id from sustc.public.record where item_name = #{itemName}")
    int getId(Record record);

    @Delete("delete from sustc.public.record")
    void deleteAll();
}
