package com.example.testdemo.contorller;

import com.alibaba.fastjson.JSON;
import com.example.testdemo.component.Result;
import com.example.testdemo.entity.Container;
import com.example.testdemo.service.SearchService;
import com.example.testdemo.utils.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/advance")
public class AdvanceController {

    @Autowired
    HttpServletResponse response;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    SearchService searchService;

    @PostMapping("/task1")
    public void getResult1(@RequestBody Container container) {
        String s;
        long b = System.currentTimeMillis();
        String key = RedisConstants.SEARCH_ADVANCE_KEY1 + container.getType();
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            s = stringRedisTemplate.opsForValue().get(key);
        } else {
            Result<Map<String, Integer>> result = searchService.getContainerAdvance(container);
            s = JSON.toJSONString(result);
            stringRedisTemplate.opsForValue().set(key, s);
            stringRedisTemplate.expire(key, RedisConstants.INSERT_TTL, TimeUnit.SECONDS);
        }
        System.out.println(s);
        try {
            response.getOutputStream().write(s.getBytes());
            response.getOutputStream().close();
        } catch (IOException e) {
            Result.error("103", e.getMessage());
        }
        long c = System.currentTimeMillis();
        System.out.println("用时" + (c - b) + "ms");
    }

    @PostMapping("/task2/{city}/{company}")
    public void getResult2(@PathVariable("city") String city,
                           @PathVariable("company") String company) {
        String s;
        long b = System.currentTimeMillis();
        String key = RedisConstants.SEARCH_ADVANCE_KEY2 + city + ":" + company;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            s = stringRedisTemplate.opsForValue().get(key);
        } else {
            Result<Map<String, Integer>> result = searchService.getGreatestCourier(city, company);
            s = JSON.toJSONString(result);
            stringRedisTemplate.opsForValue().set(key, s);
            stringRedisTemplate.expire(key, RedisConstants.INSERT_TTL, TimeUnit.SECONDS);
        }
        System.out.println(s);
        try {
            response.getOutputStream().write(s.getBytes());
            response.getOutputStream().close();
        } catch (IOException e) {
            Result.error("103", e.getMessage());
        }
        long c = System.currentTimeMillis();
        System.out.println("用时" + (c - b) + "ms");
    }

    @PostMapping("/task3/")
    public Result<String> getResult3(@RequestBody String[] companies) {
        return searchService.getResult3ByList(companies);
    }

    @GetMapping("/task3")
    public Result<Map<String, Double>> getResult3() {
        String s;
        long b = System.currentTimeMillis();
        String key = RedisConstants.SEARCH_ADVANCE_KEY3;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            s = stringRedisTemplate.opsForValue().get(key);
        } else {
            Result<Map<String, Double>> result = searchService.getResult3();
            s = JSON.toJSONString(result);
            stringRedisTemplate.opsForValue().set(key, s);
            stringRedisTemplate.expire(key, RedisConstants.INSERT_TTL, TimeUnit.SECONDS);
        }
        System.out.println(s);
        try {
            response.getOutputStream().write(s.getBytes());
            response.getOutputStream().close();
        } catch (IOException e) {
            Result.error("103", e.getMessage());
        }
        long c = System.currentTimeMillis();
        System.out.println("用时" + (c - b) + "ms");
        return searchService.getResult3();
    }
}
