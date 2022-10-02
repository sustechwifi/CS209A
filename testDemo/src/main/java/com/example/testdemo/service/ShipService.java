package com.example.testdemo.service;

import cn.hutool.core.util.StrUtil;
import com.example.testdemo.entity.Ship;
import com.example.testdemo.mapper.ShipMapper;
import com.example.testdemo.utils.RedisConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
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


    public Integer getId(Ship ship) {
        if (ship == null || StrUtil.isEmpty(ship.getName())) {
            return null;
        }
        String key = RedisConstants.INSERT_SHIP_KEY + ship.getName();
        if (idRedisTemplate.hasKey(key)) {
            return Integer.parseInt(idRedisTemplate.opsForValue().get(key));
        }
        Integer res = shipMapper.getId(ship);
        if (res == null) {
            return null;
        }
        idRedisTemplate.opsForValue().set(key, String.valueOf(res));
        idRedisTemplate.expire(key, INSERT_TTL, TimeUnit.SECONDS);
        return res;
    }

    public Integer getSearchId(Ship ship) {
        if (ship == null || StrUtil.isEmpty(ship.getName())) {
            return null;
        } else {
            return Objects.requireNonNullElse(getId(ship), -1);
        }
    }

    public String getShipById(Integer id) {
        if (id == null) {
            return null;
        }
        return shipMapper.getShipById(id).getName();
    }

    public void deleteAll() {
        shipMapper.deleteAll();
    }
}
