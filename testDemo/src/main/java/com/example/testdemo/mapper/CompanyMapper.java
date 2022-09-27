package com.example.testdemo.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyMapper {

    @Insert("insert into sustc.public.company(name) values (#{companyName}) ON CONFLICT(name) DO NOTHING")
    void addCompany(String companyName);

    @Select("select id from sustc.public.company where name = #{companyName}")
    int getId(String companyName);

    @Select("select name from sustc.public.company where id = #{id}")
    String getCompanyName(int id);

    @Delete("delete from sustc.public.company")
    void deleteAll();
}
