package com.example.testdemo.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.example.testdemo.component.PageBean;
import com.example.testdemo.component.Result;
import com.example.testdemo.dto.UserDTO;
import com.example.testdemo.entity.User;
import com.example.testdemo.mapper.UserMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.testdemo.utils.RedisConstants.LOGIN_USER_KEY;
import static com.example.testdemo.utils.RedisConstants.LOGIN_USER_TTL;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    public int addUser(User user) {
        if (user == null) {
            return 1;
        }
        userMapper.add(user);
        return 0;
    }

    public PageBean<User> selectByCondition(int PageNum, int PageSize, User user) {

        int begin = (PageNum - 1) * PageSize;

        //设置模糊表达式
        String username = user.getUsername();
        if (username != null && !"".equals(username)) {
            user.setUsername("%" + username + "%");
        }

        String nickName = user.getNickName();
        if (nickName != null && !"".equals(nickName)) {
            user.setNickName("%" + nickName + "%");
        }
        int end = begin + PageSize;
        System.out.println("start:" + begin);
        System.out.println("end:" + end);
        List<User> users = userMapper.selectByPagination(begin, end, user);
        int total = userMapper.selectTotalCount(user);

        PageBean<User> bean = new PageBean<>();
        bean.setRows(users);
        bean.setTotalCount(total);
        System.out.println(bean);
        return bean;
    }


    public int updateUser(User user) {
        if (user == null) {
            return 1;
        }
        userMapper.updateById(user);
        return 0;
    }

    public void deleteUserById(long id) {
        userMapper.deleteByUserId(id);
    }

    public void deleteUserByIds(long[] ids) {
        userMapper.deleteByIds(ids);
    }

    public Result<?> login(User user) {
        User userLogin = userMapper.userLogin(user);
        if (userLogin == null) {
            return Result.error("109", "用户名或密码错误");
        } else {
            //生成随机token
            String token = UUID.randomUUID().toString(true);
            UserDTO userDTO = BeanUtil.copyProperties(userLogin, UserDTO.class);

            Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                    CopyOptions.create()
                            .setIgnoreNullValue(true)
                            .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));

            String tokenKey = LOGIN_USER_KEY + token;

            stringRedisTemplate.opsForValue().set(tokenKey, JSONUtil.toJsonStr(userMap));
            stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.SECONDS);

            return Result.success(tokenKey);
        }
    }

    public boolean checkUser(User user) {
        User result = userMapper.checkUser(user);
        System.out.println(result);
        return result == null;
    }

    public void updateUserImg(String url, long id) {
        System.out.println(url);
        userMapper.changeImg(url, id);
    }

    public String findUserImg(String username) {
        return userMapper.findImg(username);
    }

    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }
}
