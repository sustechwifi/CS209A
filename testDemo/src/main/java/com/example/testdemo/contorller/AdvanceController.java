package com.example.testdemo.contorller;

import com.alibaba.druid.util.StringUtils;
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
    public Result<Map<String, Integer>> getResult1(@RequestBody Container container) {
        String s;
        Result<Map<String, Integer>> result;
        long b = System.currentTimeMillis();
        String key = RedisConstants.SEARCH_ADVANCE_KEY1 + (
                (StringUtils.isEmpty(container.getCode()))
                        ? container.getType() : container.getCode());
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            s = stringRedisTemplate.opsForValue().get(key);
            result = JSON.parseObject(s, Result.class);
        } else {
            result = searchService.getContainerAdvance(container);
            s = JSON.toJSONString(result);
            stringRedisTemplate.opsForValue().set(key, s);
            stringRedisTemplate.expire(key, RedisConstants.INSERT_TTL, TimeUnit.SECONDS);
        }
        System.out.println(s);
        long c = System.currentTimeMillis();
        String msg = "用时" + (c - b) + "ms";
        System.out.println(msg);
        result.setMsg(msg);
        return result;
    }

    @PostMapping("/task2/{city}/{company}")
    public Result<Map<String, Integer>> getResult2(@PathVariable("city") String city,
                                                   @PathVariable("company") String company) {
        String s;
        Result<Map<String, Integer>> result;
        long b = System.currentTimeMillis();
        String key = RedisConstants.SEARCH_ADVANCE_KEY2 + city + ":" + company;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            s = stringRedisTemplate.opsForValue().get(key);
            result = JSON.parseObject(s, Result.class);
        } else {
            result = searchService.getGreatestCourier(city, company);
            s = JSON.toJSONString(result);
            stringRedisTemplate.opsForValue().set(key, s);
            stringRedisTemplate.expire(key, RedisConstants.INSERT_TTL, TimeUnit.SECONDS);
        }
        System.out.println(s);
        long c = System.currentTimeMillis();
        String msg = "用时" + (c - b) + "ms";
        System.out.println(msg);
        result.setMsg(msg);
        return result;
    }

    @PostMapping("/task3/")
    public Result<String> getResult3(@RequestBody String[] companies) {
        return searchService.getResult3ByList(companies);
    }

    @GetMapping("/task3")
    public Result<Map<String, Double>> getResult3() {
        String s;
        Result<Map<String, Double>> result;
        long b = System.currentTimeMillis();
        String key = RedisConstants.SEARCH_ADVANCE_KEY3;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            s = stringRedisTemplate.opsForValue().get(key);
            result = JSON.parseObject(s, Result.class);
        } else {
            result = searchService.getResult3();
            s = JSON.toJSONString(result);
            stringRedisTemplate.opsForValue().set(key, s);
            stringRedisTemplate.expire(key, RedisConstants.INSERT_TTL, TimeUnit.SECONDS);
        }
        System.out.println(s);
        long c = System.currentTimeMillis();
        String msg = "用时" + (c - b) + "ms";
        System.out.println(msg);
        result.setMsg(msg);
        return result;
    }
}
