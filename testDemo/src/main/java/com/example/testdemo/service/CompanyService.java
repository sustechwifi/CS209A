package com.example.testdemo.service;

import cn.hutool.core.util.StrUtil;
import com.example.testdemo.entity.Company;
import com.example.testdemo.mapper.CompanyMapper;
import com.example.testdemo.mapper.UserMapper;
import com.example.testdemo.utils.RedisConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
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


    public Integer getId(String name) {
        if (StrUtil.isEmpty(name)) {
            return null;
        }
        String key = INSERT_COMPANY_KEY + name;
        if (idRedisTemplate.hasKey(key)) {
            return Integer.parseInt(idRedisTemplate.opsForValue().get(key));
        }
        Integer res = companyMapper.getId(name);
        if (res == null) {
            return null;
        }
        idRedisTemplate.opsForValue().set(key, String.valueOf(res));
        idRedisTemplate.expire(key, INSERT_TTL, TimeUnit.SECONDS);
        return res;
    }

    public Integer getSearchId(String name) {
        if (StrUtil.isEmpty(name)) {
            return null;
        } else {
            Integer id = getId(name);
            return Objects.requireNonNullElse(id, -1);
        }
    }

    public String getName(Integer id) {
        if (id == null) {
            return null;
        }
        return companyMapper.getCompanyName(id);
    }

    public void deleteAll() {
        companyMapper.deleteAll();
    }
}
