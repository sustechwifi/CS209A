package com.example.testdemo.service;

import com.alibaba.druid.util.StringUtils;
import com.example.testdemo.entity.City;
import com.example.testdemo.mapper.CityMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    public int getId(City city) {
        String key = INSERT_CITY_KEY + city.getName();
        if (idRedisTemplate.hasKey(key)) {
            return Integer.parseInt(idRedisTemplate.opsForValue().get(key));
        }
        int res = cityMapper.getId(city.getName());
        idRedisTemplate.opsForValue().set(key, String.valueOf(res));
        idRedisTemplate.expire(key, INSERT_TTL, TimeUnit.SECONDS);
        return res;
    }

    public void deleteAll() {
        cityMapper.deleteAll();
    }
}
