package com.example.testdemo.mapper;

import com.example.testdemo.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
  @Insert("insert into \"user\" (username, password, nick_name, sex, isadmin, img)values (#{username},#{password},#{nickName},#{sex},false,'')")
  void add(User user);

  @Select("select * from \"user\" where #{id} = id")
  Integer selectById(int id);

  @Update("update user set password = #{password},nick_name = #{nickName},sex = #{sex} where id = #{id}")
  int updateById(User user);

  List<User> selectByPagination(@Param("begin")int begin,@Param("end")int end,@Param("user")User user);

  Integer selectTotalCount(@Param("user")User user);

  @Delete("delete from \"user\" where id = #{id}")
  int deleteByUserId(long id);

  User userLogin(User user);

  User checkUser(User user);

  void deleteByIds(@Param("ids") long[] ids);

  @Update("update \"user\" set img = #{img} where id = #{id}")
  void changeImg(@Param("img")String img,@Param("id")long id);

  @Select("select img from \"user\" where username = #{username} limit 1")
  String findImg(String username);

    User getUserById(Long id);
}
