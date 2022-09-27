package com.example.testdemo.service;

import com.example.testdemo.mapper.CompanyMapper;
import com.example.testdemo.mapper.UserMapper;
import com.example.testdemo.utils.RedisConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.example.testdemo.utils.RedisConstants.*;

@Service
public class CompanyService {
    @Resource
    CompanyMapper companyMapper;

    @Resource
    StringRedisTemplate idRedisTemplate;

    public int add(String name) {
        companyMapper.addCompany(name);
        return companyMapper.getId(name);
    }


    public int getId(String name) {
        String key = INSERT_COMPANY_KEY + name;
        if (idRedisTemplate.hasKey(key)) {
            return Integer.parseInt(idRedisTemplate.opsForValue().get(key));
        }
        int res = companyMapper.getId(name);
        idRedisTemplate.opsForValue().set(key, String.valueOf(res));
        idRedisTemplate.expire(key, INSERT_TTL, TimeUnit.SECONDS);
        return res;
    }

    public String getName(int id) {
        return companyMapper.getCompanyName(id);
    }

    public void deleteAll() {
        companyMapper.deleteAll();
    }
}
