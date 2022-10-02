package com.example.testdemo.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.util.StringUtils;
import com.example.testdemo.entity.City;
import com.example.testdemo.mapper.CityMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.testdemo.utils.RedisConstants.INSERT_CITY_KEY;
import static com.example.testdemo.utils.RedisConstants.INSERT_TTL;

@Service
public class CityService {
    @Resource
    CityMapper cityMapper;

    @Resource
    StringRedisTemplate idRedisTemplate;

    public int add(City city) {
        cityMapper.add(city);
        return cityMapper.getId(city.getName());
    }

    public Integer getId(City city) {
        if (city == null || StrUtil.isEmpty(city.getName())) {
            return null;
        }
        String key = INSERT_CITY_KEY + city.getName();
        if (idRedisTemplate.hasKey(key)) {
            return Integer.parseInt(idRedisTemplate.opsForValue().get(key));
        }
        Integer res = cityMapper.getId(city.getName());
        if (res == null) {
            return null;
        }
        idRedisTemplate.opsForValue().set(key, String.valueOf(res));
        idRedisTemplate.expire(key, INSERT_TTL, TimeUnit.SECONDS);
        return res;
    }

    public Integer getSearchId(City city) {
        if (city == null || StrUtil.isEmpty(city.getName())) {
            return null;
        } else {
            return Objects.requireNonNullElse(getId(city), -1);
        }
    }

    public String getCityName(Integer id) {
        if (id == null) {
            return "";
        }
        return cityMapper.getCityName(id);
    }

    public void deleteAll() {
        cityMapper.deleteAll();
    }
}
