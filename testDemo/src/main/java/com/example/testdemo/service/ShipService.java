package com.example.testdemo.service;

import com.example.testdemo.entity.Ship;
import com.example.testdemo.mapper.ShipMapper;
import com.example.testdemo.utils.RedisConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.example.testdemo.utils.RedisConstants.INSERT_TTL;


@Service
public class ShipService {
    @Resource
    ShipMapper shipMapper;

    @Resource
    StringRedisTemplate idRedisTemplate;

    public int add(Ship ship) {
        shipMapper.add(ship);
        return shipMapper.getId(ship);
    }


    public int getId(Ship ship) {
        String key = RedisConstants.INSERT_SHIP_KEY + ship.getName();
        if (idRedisTemplate.hasKey(key)) {
            return Integer.parseInt(idRedisTemplate.opsForValue().get(key));
        }
        int res = shipMapper.getId(ship);
        idRedisTemplate.opsForValue().set(key, String.valueOf(res));
        idRedisTemplate.expire(key, INSERT_TTL, TimeUnit.SECONDS);
        return res;
    }

    public void deleteAll() {
        shipMapper.deleteAll();
    }
}
