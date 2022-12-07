package com.example.project.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReleaseMapper {
    @Insert("insert into cs209a.public.release (content,repo,owner) values (#{content},#{repo},#{owner})")
    void add(@Param("content") String content, @Param("repo") String repo, @Param("owner")String owner);


    @Select("select content from cs209a.public.release where repo = #{repo} and owner = #{owner}")
    List<String> get(@Param("repo") String repo, @Param("owner") String owner);
}
