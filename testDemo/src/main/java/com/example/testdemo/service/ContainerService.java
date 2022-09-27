package com.example.testdemo.service;

import com.alibaba.druid.util.StringUtils;
import com.example.testdemo.entity.Container;
import com.example.testdemo.mapper.ContainerMapper;
import com.example.testdemo.utils.RedisConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class ContainerService {
    @Resource
    ContainerMapper containerMapper;

    @Resource
    StringRedisTemplate idRedisTemplate;

    public int add(Container container) {
        containerMapper.add(container);
        return containerMapper.getId(container);
    }

    public int getId(Container container) {
        String key = RedisConstants.INSERT_CONTAINER_KEY + container.getCode();
        if (idRedisTemplate.hasKey(key)) {
            return Integer.parseInt(idRedisTemplate.opsForValue().get(key));
        }
        int res = containerMapper.getId(container);
        idRedisTemplate.opsForValue().set(key, String.valueOf(res));
        idRedisTemplate.expire(key, RedisConstants.INSERT_TTL, TimeUnit.SECONDS);
        return res;
    }

    public void deleteAll() {
        containerMapper.deleteAll();
    }
}
