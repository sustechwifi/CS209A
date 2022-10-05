package com.example.testdemo.service;

import com.alibaba.druid.util.StringUtils;
import com.example.testdemo.entity.Container;
import com.example.testdemo.mapper.ContainerMapper;
import com.example.testdemo.utils.RedisConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ContainerService {
    @Resource
    ContainerMapper containerMapper;

    @Resource
    StringRedisTemplate idRedisTemplate;

    public int add(Container container) {
        Integer t = getId(container);
        if (t != null) {
            return t;
        }
        containerMapper.add(container);
        return getId(container);
    }

    public Integer getId(Container container) {
        if (container == null || StringUtils.isEmpty(container.getCode())) {
            return null;
        }
        String key = RedisConstants.INSERT_CONTAINER_KEY + container.getCode();
        if (idRedisTemplate.hasKey(key)) {
            return Integer.parseInt(idRedisTemplate.opsForValue().get(key));
        }
        Integer res = containerMapper.getId(container);
        if (res == null) {
            return null;
        }
        idRedisTemplate.opsForValue().set(key, String.valueOf(res));
        idRedisTemplate.expire(key, RedisConstants.INSERT_TTL, TimeUnit.SECONDS);
        return res;
    }

    public Container getContainerById(Integer id) {
        if (id == null) {
            return null;
        }
        return containerMapper.getContainerById(id);
    }

    public Container formContainer(Container container) {
        return Objects.requireNonNullElseGet(container, Container::new);
    }

    public Integer[] getIds(Container container) {
        if (!StringUtils.isEmpty(container.getCode())) {
            Integer id = getId(container);
            if (id == null) {
                return null;
            }
            return new Integer[]{id};
        } else {
            List<Integer> ids = containerMapper.getIds(container.getType());
            if (ids.isEmpty()) {
                return null;
            } else {
                return ids.toArray(Integer[]::new);
            }
        }
    }

    public void deleteAll() {
        containerMapper.deleteAll();
    }

    public Map<String, Integer> getOldestContainer(String type) {
        int res = containerMapper.getOldestContainer(type);
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put(type, res);
        return hashMap;
    }
}
