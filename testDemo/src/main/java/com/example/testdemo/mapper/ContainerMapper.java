package com.example.testdemo.mapper;

import com.example.testdemo.entity.Container;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContainerMapper {

    @Insert("insert into sustc.public.container (code, type) VALUES (#{code},#{type}) on conflict do nothing")
    void add(Container container);

    @Select("select id from sustc.public.container where code = #{code}")
    Integer getId (Container container);

    @Delete("delete from sustc.public.container")
    void deleteAll();


    Container getContainerById(Integer id);


    List<Integer> getIds(String type);

    int getOldestContainer(String type);
}
