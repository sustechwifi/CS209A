package com.example.testdemo.contorller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.example.testdemo.component.PageBean;
import com.example.testdemo.component.Result;
import com.example.testdemo.component.RowRecord;
import com.example.testdemo.service.SearchService;
import com.example.testdemo.utils.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    HttpServletResponse response;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    SearchService searchService;

    @GetMapping("/batch")
    public void batchSearch(@RequestParam String item,
                            @RequestParam int type,
                            @RequestParam String search,
                            @RequestParam int begin,
                            @RequestParam int pageSize) {
        long b = System.currentTimeMillis();
        PageBean<RowRecord> userPageBean = searchService.handleBatchSearch(item, type, search, begin, pageSize);
        String s = JSON.toJSONString(userPageBean);
        System.out.println(s);
        try {
            response.getOutputStream().write(s.getBytes());
            response.getOutputStream().close();
        } catch (IOException e) {
            Result.error("103", e.getMessage());
        }
        long c = System.currentTimeMillis();
        System.out.println(c - b);
    }


    @PostMapping("/precise/{type}/{size}/{begin}")
    public void preciseSearch(@RequestBody RowRecord record,
                              @PathVariable("type") int type,
                              @PathVariable("size") int size,
                              @PathVariable("begin") int begin) {
        String s;
        long b = System.currentTimeMillis();
        System.out.println(record);
        String key = RedisConstants.SEARCH_PRECISE_KEY + JSON.toJSONString(record) + RedisConstants.formatPageBean(begin, size);
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            s = stringRedisTemplate.opsForValue().get(key);
        } else {
            PageBean<RowRecord> rowRecordPageBean = searchService.handlePreciseSearch(type, record, begin, size);
            rowRecordPageBean.setMsg("用时：" + (System.currentTimeMillis() - b) + "ms");
            s = JSON.toJSONString(rowRecordPageBean);
            stringRedisTemplate.opsForValue().set(key, s);
            stringRedisTemplate.expire(key, 300, TimeUnit.SECONDS);
        }
        try {
            System.out.println(s);
            response.getOutputStream().write(s.getBytes());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("用时：" + (System.currentTimeMillis() - b) + "ms");
    }
}
