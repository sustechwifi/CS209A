package com.example.testdemo.contorller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.example.testdemo.component.PageBean;
import com.example.testdemo.component.Result;
import com.example.testdemo.component.RowRecord;
import com.example.testdemo.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    HttpServletResponse response;

    @Resource
    SearchService searchService;

    @GetMapping("/batch")
    public Result<String> batchSearch(@RequestParam String item,
                                      @RequestParam int type,
                                      @RequestParam String search,
                                      @RequestParam int begin,
                                      @RequestParam int pageSize) {
        if (StringUtils.isEmpty(item) || StringUtils.isEmpty(search)) {
            Result.error("303", "invalid search");
        }
        PageBean<RowRecord> userPageBean = searchService.handleBatchSearch(item, type, search, begin, pageSize);

        String s = JSON.toJSONString(userPageBean);
        try {
            response.getOutputStream().write(s.getBytes());
            response.getOutputStream().close();
        } catch (IOException e) {
            Result.error("103", e.getMessage());
        }
        return Result.success("result set:" + userPageBean.getRows());
    }


    @PostMapping("/precise/{type}/{size}/{begin}")
    public void preciseSearch(@RequestBody RowRecord record,
                              @PathVariable("type") int type,
                              @PathVariable("size") int size,
                              @PathVariable("begin") int begin) {
        long b = System.currentTimeMillis();
        System.out.println(record);
        PageBean<RowRecord> rowRecordPageBean = searchService.handlePreciseSearch(type, record, begin, size);
        long c = System.currentTimeMillis();
        rowRecordPageBean.setMsg("用时：" + (c - b) + "ms");
        String s = JSON.toJSONString(rowRecordPageBean);
        try {
            System.out.println(s);
            response.getOutputStream().write(s.getBytes());
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(c - b);
    }
}
