package com.example.testdemo.service;

import com.example.testdemo.component.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class DeleteService {
    @Resource
    RecordService recordService;
    @Resource
    HandleService handleService;
    @Resource
    TransitService transitService;

    @Transactional
    public Result<?> deleteByRecordIds(Integer[] ids) {
        long a = System.currentTimeMillis();
        try {
            transitService.deleteByRecordIds(ids);
            handleService.deleteByRecordIds(ids);
            recordService.deleteByIds(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("402", e.getMessage());
        }
        Result<?> result = new Result<>();
        result.setCode("0");
        result.setMsg("用时：" + (System.currentTimeMillis() - a) + "ms");
        return result;
    }


}
