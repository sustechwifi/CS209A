package com.example.testdemo.mapper;

import com.example.testdemo.component.RowRecord;
import com.example.testdemo.entity.Record;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordMapper {

    @Insert("insert into sustc.public.record (log_time, item_name, item_class, item_price, company_id, container_id, ship_id, state) " +
            "VALUES (#{logTime},#{itemName},#{itemClass},#{itemPrice},#{companyId},#{containerId},#{shipId},#{state}) on conflict do nothing ")
    void add(Record record);

    @Select("select id from sustc.public.record where item_name = #{itemName}")
    int getId(Record record);

    @Delete("delete from sustc.public.record")
    void deleteAll();

    List<Record> getRecordsByModel(@Param("record") Record record, @Param("begin") int begin, @Param("size") int size);


    List<Integer> getIds(@Param("record") Record record);


    Record getRecordById(@Param("id") int id);

    List<Record> getRecordByIds(@Param("ids") Integer[] ids);

    List<Integer> getIdsByType(int type);

    void deleteByIds(Integer[] ids);

    @Select("select count(id) from sustc.public.record")
    int getCount();

    @Select("select id from sustc.public.record where item_name = #{itemName}")
    int getIdByItemName(String itemName);

    List<Integer> getIdsByShip(String condition, int type);

    List<Integer> getIdsByContainer(String condition, int type);

    List<Integer> getIdsByItemClass(String condition, int type);


    List<Integer> getIdsByCompany(String condition, int type);

    void updateByModel(@Param("r") RowRecord r, @Param("type") int type);

    void updateToExported(@Param("id") int id, @Param("shipId") int shipId,
                          @Param("containerId") int containerId);

    @Update("update sustc.public.record set state = 3 where id = #{id}")
    void updateToUnDelivery(Integer id);

    @Update("update sustc.public.record set state = 4 where id = #{id}")
    void updateToFinished(Integer id);

    List<Integer> getAllUnFinished();

    List<Integer> getAllUnExported();

    List<Integer> getAllExported();

}
