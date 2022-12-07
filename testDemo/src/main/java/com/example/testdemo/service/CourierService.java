package com.example.testdemo.service;

import com.alibaba.druid.util.StringUtils;
import com.example.testdemo.entity.Courier;
import com.example.testdemo.mapper.CourierMapper;
import com.example.testdemo.utils.RedisConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class CourierService {
    @Resource
    CourierMapper courierMapper;

    @Resource
    StringRedisTemplate idRedisTemplate;

    public int add(Courier courier) {
        Integer t = getId(courier);
        if (t != null) {
            return t;
        }
        courierMapper.add(courier);
        return getId(courier);
    }

    public Integer getId(Courier courier) {
        if (StringUtils.isEmpty(courier.getName()) || StringUtils.isEmpty(courier.getPhoneNumber())) {
            return null;
        }
        String key = RedisConstants.INSERT_COURIER_KEY + courier.getName() + "|" + courier.getPhoneNumber();
        if (idRedisTemplate.hasKey(key)) {
            return Integer.parseInt(idRedisTemplate.opsForValue().get(key));
        }
        Integer res = courierMapper.getId(courier);
        if (res == null) {
            return null;
        }
        idRedisTemplate.opsForValue().set(key, String.valueOf(res));
        idRedisTemplate.expire(key, RedisConstants.INSERT_TTL, TimeUnit.SECONDS);
        return res;
    }

    public boolean checkAgeAndGender(Courier courier, Integer age, Integer gender) {
        if (age == null && gender == null) {
            return false;
        } else if (age == null || gender == null) {
            return age == null ? !gender.equals(courier.getGender()) : !age.equals(courier.getBirthYear());
        } else {
            return !age.equals(courier.getBirthYear()) || !gender.equals(courier.getGender());
        }
    }

    public Integer getSearchId(Courier courier) {
        if (StringUtils.isEmpty(courier.getName()) || StringUtils.isEmpty(courier.getPhoneNumber())) {
            return null;
        } else {
            return Objects.requireNonNullElse(getId(courier), -1);
        }
    }

    public void deleteAll() {
        courierMapper.deleteAll();
    }

    public Courier getCourier(Courier courier) {
        return courierMapper.getCourier(courier);
    }

    public Courier getCourierById(Integer id) {
        if (id == null) {
            return null;
        }
        return courierMapper.selectById(id);
    }

    public Courier formCourier(Courier courier) {
        return Objects.requireNonNullElseGet(courier, Courier::new);
    }

    public Map<String, Integer> getGreatestCourier(String city, String company) {
        return courierMapper.getGreatestCourier(city, company);
    }
}
