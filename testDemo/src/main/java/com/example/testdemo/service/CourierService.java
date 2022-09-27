package com.example.testdemo.service;

import com.example.testdemo.entity.Courier;
import com.example.testdemo.mapper.CourierMapper;
import com.example.testdemo.utils.RedisConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class CourierService {
    @Resource
    CourierMapper courierMapper;

    @Resource
    StringRedisTemplate idRedisTemplate;

    public int add(Courier courier) {
        courierMapper.add(courier);
        return courierMapper.getId(courier);
    }

    public int getId(Courier courier) {
        String key = RedisConstants.INSERT_COURIER_KEY + courier.getName() + "|" + courier.getPhoneNumber();
        if (idRedisTemplate.hasKey(key)) {
            return Integer.parseInt(idRedisTemplate.opsForValue().get(key));
        }
        int res = courierMapper.getId(courier);
        idRedisTemplate.opsForValue().set(key, String.valueOf(res));
        idRedisTemplate.expire(key, RedisConstants.INSERT_TTL, TimeUnit.SECONDS);
        return res;
    }

    public void deleteAll() {
        courierMapper.deleteAll();
    }

}
